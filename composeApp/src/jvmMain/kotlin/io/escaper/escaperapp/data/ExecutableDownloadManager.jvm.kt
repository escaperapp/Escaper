package io.escaper.escaperapp.data

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentLength
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okio.Path.Companion.toOkioPath
import io.escaper.escaperapp.data.networking.ensureBinPatternFiles
import io.escaper.escaperapp.platform.Platform
import io.escaper.escaperapp.platform.PlatformProvider
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.deleteRecursively
import kotlin.streams.asSequence

internal actual class ExecutableDownloadManager actual constructor(
    private val pathsProvider: PathsProvider,
    private val httpClient: HttpClient,
    private val zapretUrlProvider: ZapretUrlProvider,
    private val zipExtractor: ZipExtractor,
) {
    private val logger = Logger.withTag(tag = "ExecutableDownloadManager")
    private val _isDownloading = MutableStateFlow(false)
    actual val isDownloading = _isDownloading.asStateFlow()

    private suspend fun downloadFileDirect(
        url: String,
        dest: String,
        onProgress: (percent: Int, downloaded: Long, total: Long?) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val destPath = dest.asPath()
            try {
                destPath.parent.createDirectories()

                val response: HttpResponse = httpClient.get(url)

                when (response.status) {
                    HttpStatusCode.MovedPermanently,
                    HttpStatusCode.Found -> {
                        val location = response.headers[HttpHeaders.Location]
                            ?: error("Redirect without Location header")

                        return@withContext downloadFileDirect(location, dest, onProgress)
                    }

                    HttpStatusCode.OK -> {
                        val totalSize = response.contentLength()
                        val channel = response.bodyAsChannel()

                        Files.newOutputStream(
                            destPath,
                            StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING
                        ).use { output ->

                            val buffer = ByteArray(8192)
                            var downloaded = 0L

                            while (!channel.isClosedForRead) {
                                val bytesRead = channel.readAvailable(buffer)
                                if (bytesRead == -1) break

                                output.write(buffer, 0, bytesRead)
                                downloaded += bytesRead

                                val percent = totalSize?.let {
                                    ((downloaded * 100) / it).toInt()
                                } ?: 0

                                onProgress(percent, downloaded, totalSize)
                            }
                        }
                    }

                    else -> {
                        throw RuntimeException("HTTP ${response.status.value}")
                    }
                }

            } catch (_: Exception) {
                runCatching { destPath.deleteIfExists() }
            }
        }
    }

    @OptIn(ExperimentalPathApi::class)
    actual suspend fun downloadAndExtractBinaries(): DownloadResult = withContext(Dispatchers.IO) {
        val binaryFolder = pathsProvider.getExecutableFolder().asPath()
        val binaryFile = pathsProvider.getExecutablePath().asPath()
        val additionalResourcesFolder = pathsProvider.getResourcePath().asPath()
        val tempDirPath = pathsProvider.getTempDir().asPath()

        if (_isDownloading.value) {
            return@withContext DownloadResult(false, "Already downloading")
        }

        _isDownloading.value = true

        try {
            // Windows Defender exclusion attempt
            if (PlatformProvider.platform == Platform.Windows) {
                runCatching {
                    ProcessBuilder(
                        "powershell",
                        "-command",
                        "Add-MpPreference -ExclusionPath '${binaryFile}'"
                    ).start().waitFor()
                }
            }

            // Cleanup temp
            tempDirPath.deleteRecursively()

            Files.createDirectories(binaryFolder)
            Files.createDirectories(additionalResourcesFolder)
            Files.createDirectories(tempDirPath)

            val downloadUrl = zapretUrlProvider.getLatestZapretUrl()
            logger.i("Trying to download from url: $downloadUrl")
            val zipPath = tempDirPath.resolve("zapret.zip")

            Files.deleteIfExists(zipPath)

            // Download
            downloadFileDirect(
                url = downloadUrl,
                dest = zipPath.absolutePathString()
            ) { percent, downloaded, total ->
                println("Downloading: $percent% ($downloaded/$total)")
            }

            when (PlatformProvider.jvmPlatform) {
                Platform.Linux -> TODO()
                Platform.MacOS -> extractMac(
                    zipPath = zipPath,
                    tempDir = tempDirPath,
                    destinationPath = binaryFile
                )

                Platform.Windows -> extractWindows(
                    zipPath = zipPath,
                    tempDir = tempDirPath,
                    platformDir = pathsProvider.getExecutableFolder().asPath()
                )
            }

            tempDirPath.deleteRecursively()

            _isDownloading.value = false

            DownloadResult(success = true)

        } catch (e: Exception) {
            logger.e(messageString = "Binary download failed", throwable = e)

            _isDownloading.value = false

            runCatching {
                tempDirPath.deleteRecursively()
            }

            DownloadResult(false, e.message)
        }
    }

    private suspend fun extractWindows(
        zipPath: Path,
        tempDir: Path,
        platformDir: Path
    ) {
        withContext(Dispatchers.IO) {
            zipExtractor.extractZip(zipPath.toOkioPath(), tempDir.toOkioPath())

            val sourceDir = Files.walk(tempDir)
                .asSequence()
                .firstOrNull { it.fileName.toString() == "windows-x86_64" }
                ?: throw IllegalStateException("windows-x86_64 not found in archive")

            Files.list(sourceDir).use { files ->
                for (file in files) {
                    logger.d("Trying to copy file: ${file.absolutePathString()}")
                    Files.copy(
                        file,
                        platformDir.resolve(file.fileName),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            }

            ensureBinPatternFiles(platformDir.absolutePathString())
        }
    }

    private suspend fun extractMac(
        zipPath: Path,
        tempDir: Path,
        destinationPath: Path,
    ) {
        withContext(Dispatchers.IO) {
            zipExtractor.extractZip(zipPath.toOkioPath(), tempDir.toOkioPath())

            val folderFromZip = tempDir.toFile().listFiles()
                .find { it.isDirectory }?.toPath() ?: error("Failed to extract ZIP file")

            val tpws = Files.walk(folderFromZip.resolve("binaries"))
                .asSequence().find {
                    it.absolutePathString().contains("mac") &&
                            it.fileName.toString() == "tpws"
                } ?: error("tpws binary not found")

            Files.copy(
                tpws,
                destinationPath,
                StandardCopyOption.REPLACE_EXISTING
            )
            destinationPath.toFile().setExecutable(true)
        }
    }
}
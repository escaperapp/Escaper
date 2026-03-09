package io.escaper.escaperapp.data

import io.escaper.escaperapp.platform.Platform
import io.escaper.escaperapp.platform.PlatformProvider
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

actual class PathsProvider actual constructor() {

    private val userDataDir: Path by lazy {
        getUserDataDirPath()
    }

    private fun getBinDir(): Path =
        userDataDir.resolve("bin")

    private fun getPlatformBinDirImpl(): Path =
        getBinDir().resolve(
            when (PlatformProvider.jvmPlatform) {
                Platform.Windows -> "win32"
                Platform.MacOS -> "darwin"
                Platform.Linux -> "linux"
            }
        )

    actual fun getExecutableFolder() = getPlatformBinDirImpl().absolutePathString()

    actual fun getExecutablePath(): String {
        val dir = getPlatformBinDirImpl()

        val path = when (PlatformProvider.jvmPlatform) {
            Platform.MacOS -> {
                val arch = System.getProperty("os.arch")
                val archBinary =
                    if (arch.contains("aarch64") || arch.contains("arm"))
                        dir.resolve("tpws_arm64")
                    else
                        dir.resolve("tpws_x86_64")

                if (Files.exists(archBinary)) archBinary
                else dir.resolve("tpws")
            }

            Platform.Windows -> dir.resolve("winws.exe")

            Platform.Linux -> TODO()
        }
        return path.absolutePathString()
    }

    actual fun getResourcePath(): String {
        // Equivalent of: app.getPath('userData') + /bin/platform
        val userDataDir = getUserDataDirPath()
        return userDataDir.resolve("bin").absolutePathString()
    }

    actual fun getTempDir(appSubDir: String): String {
        val systemTemp = System.getProperty("java.io.tmpdir")
        val tempDir = Paths.get(systemTemp, appSubDir)

        Files.createDirectories(tempDir) // ensure it exists
        return tempDir.absolutePathString()
    }
}
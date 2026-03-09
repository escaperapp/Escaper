package io.escaper.escaperapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.SYSTEM
import okio.buffer
import okio.openZip
import okio.use

internal class ZipExtractor {
    suspend fun extractZip(
        zipPath: Path,
        destPath: Path,
    ) {
        withContext(Dispatchers.IO) {
            val zipFileSystem = FileSystem.SYSTEM.openZip(zipPath)
            val fileSystem = FileSystem.SYSTEM
            val paths = zipFileSystem.listRecursively("/".toPath())
                .filter { zipFileSystem.metadata(it).isRegularFile }
                .toList()

            paths.forEach { zipFilePath ->
                zipFileSystem.source(zipFilePath).buffer().use { source ->
                    val relativeFilePath = zipFilePath.toString().trimStart('/')
                    val fileToWrite = destPath.resolve(relativeFilePath)
                    fileToWrite.createParentDirectories()
                    fileSystem.sink(fileToWrite).buffer().use { sink ->
                        val bytes = sink.writeAll(source)
                        println("Unzipped: $relativeFilePath to $fileToWrite; $bytes bytes written")
                    }
                }
            }
        }
    }

    private fun Path.createParentDirectories() {
        this.parent?.let { parent ->
            FileSystem.SYSTEM.createDirectories(parent)
        }
    }
}
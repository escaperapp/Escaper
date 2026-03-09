package io.escaper.escaperapp.data

import io.escaper.escaperapp.APP_NAME
import io.escaper.escaperapp.platform.Platform
import io.escaper.escaperapp.platform.PlatformProvider
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.io.path.absolutePathString

actual fun checkFileExists(absolutePath: String): Boolean {
    return Files.exists(absolutePath.asPath())
}

actual fun writeStringToFile(absolutePath: String, content: String) {
    Files.writeString(
        absolutePath.asPath(),
        content
    )
}

internal fun String.asPath(): Path = Path.of(this)

actual fun getUserDataDir(): String = getUserDataDirPath().absolutePathString()

fun getUserDataDirPath(appName: String = APP_NAME): Path {
    val home = System.getProperty("user.home")

    val dir = when (PlatformProvider.jvmPlatform) {
        Platform.Linux -> Paths.get(home, ".config", appName)
        Platform.MacOS -> {
            Paths.get(home, "Library", "Application Support", appName)
        }

        Platform.Windows -> {
            Paths.get(System.getenv("APPDATA") ?: home, appName)
        }
    }
    Files.createDirectories(dir)
    return dir
}

actual fun createDirectories(absolutePath: String) {
    Files.createDirectories(absolutePath.asPath())
}

actual fun resolveFile(path: String, fileName: String): String {
    return path.asPath().resolve(fileName).absolutePathString()
}

actual fun writeBytesToFile(absolutePath: String, content: ByteArray) {
    Files.write(
        absolutePath.asPath(),
        content,
        // TODO: Extract to parameters?
        StandardOpenOption.WRITE,
        StandardOpenOption.CREATE
    )
}
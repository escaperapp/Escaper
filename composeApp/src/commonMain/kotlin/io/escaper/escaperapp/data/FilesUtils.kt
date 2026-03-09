package io.escaper.escaperapp.data

expect fun checkFileExists(
    absolutePath: String,
): Boolean

expect fun writeStringToFile(
    absolutePath: String,
    content: String,
)

expect fun writeBytesToFile(
    absolutePath: String,
    content: ByteArray,
)

expect fun getUserDataDir(): String

expect fun createDirectories(
    absolutePath: String,
)

expect fun resolveFile(
    path: String,
    fileName: String,
): String
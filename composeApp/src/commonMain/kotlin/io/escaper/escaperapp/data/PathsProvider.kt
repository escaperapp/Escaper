package io.escaper.escaperapp.data

expect class PathsProvider constructor() {
    fun getExecutablePath(): String
    fun getExecutableFolder(): String

    fun getResourcePath(): String

    fun getTempDir(appSubDir: String = "escaper-temp"): String
}
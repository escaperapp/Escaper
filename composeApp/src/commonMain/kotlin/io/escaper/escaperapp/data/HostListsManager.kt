package io.escaper.escaperapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import io.escaper.escaperapp.data.hostlists.HOST_LIST_DISCORD
import io.escaper.escaperapp.data.hostlists.HOST_LIST_GENERAL
import io.escaper.escaperapp.data.hostlists.HOST_LIST_GOOGLE

internal class HostListsManager {

    private val userDataDir by lazy {
        getUserDataDir()
    }

    private val listsDir = resolveFile(userDataDir, "lists")

    suspend fun ensure(): String {
        return withContext(Dispatchers.IO) {
            createDirectories(listsDir)

            writeIfMissing("list-general.txt", HOST_LIST_GENERAL)
            writeIfMissing("list-google.txt", HOST_LIST_GOOGLE)
            writeIfMissing("list-discord.txt", HOST_LIST_DISCORD)

            listsDir
        }
    }

    private fun writeIfMissing(name: String, content: String) {
        val file = resolveFile(listsDir, name)
        if (!checkFileExists(file)) {
            writeStringToFile(file, content)
        }
    }
}

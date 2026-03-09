package io.escaper.escaperapp.data.networking

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import io.escaper.escaperapp.data.checkFileExists
import io.escaper.escaperapp.data.createDirectories
import io.escaper.escaperapp.data.resolveFile
import io.escaper.escaperapp.data.writeBytesToFile

suspend fun ensureBinPatternFiles(platformDir: String) {
    withContext(Dispatchers.IO) {
        createDirectories(platformDir)

        val files: Map<String, () -> ByteArray> = mapOf(
            "quic_initial_www_google_com.bin" to { generateFakeQuicInitial() },
            "tls_clienthello_www_google_com.bin" to { generateFakeTlsClientHello("www.google.com") },
            "tls_clienthello_4pda_to.bin" to { generateFakeTlsClientHello("4pda.to") },
            "tls_clienthello_max_ru.bin" to { generateFakeTlsClientHello("max.ru") }
        )

        for ((filename, generator) in files) {
            val filePath = resolveFile(platformDir, filename)

            if (!checkFileExists(filePath)) {
                runCatching {
                    writeBytesToFile(
                        filePath,
                        generator()
                    )
                }
            }
        }
    }
}

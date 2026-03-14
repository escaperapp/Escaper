package io.escaper.escaperapp.data

import io.ktor.client.plugins.logging.Logger

internal class KtorKermitLogger : Logger {
    val kermit = co.touchlab.kermit.Logger.withTag("KtorLogger")
    override fun log(message: String) {
        kermit.i(message)
    }
}
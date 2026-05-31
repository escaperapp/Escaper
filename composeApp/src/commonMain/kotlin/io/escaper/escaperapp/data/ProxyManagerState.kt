package io.escaper.escaperapp.data

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class ProxyManagerState(
    val isConnected: Boolean,
    val connectedSince: LocalDateTime?,
) {
    companion object {
        val Disconnected = ProxyManagerState(
            isConnected = false,
            connectedSince = null,
        )

        fun connected() = ProxyManagerState(
            isConnected = true,
            connectedSince = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }
}

package io.escaper.escaperapp.data

import kotlinx.datetime.LocalDateTime

data class ProxyManagerState(
    val isConnected: Boolean,
    val connectedSince: LocalDateTime?,
) {
    companion object {
        val Initial = ProxyManagerState(
            isConnected = false,
            connectedSince = null,
        )
    }
}

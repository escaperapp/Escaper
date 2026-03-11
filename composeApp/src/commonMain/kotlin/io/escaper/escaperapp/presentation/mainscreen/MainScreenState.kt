package io.escaper.escaperapp.presentation.mainscreen

import androidx.compose.runtime.Immutable
import io.escaper.escaperapp.domain.Strategy

@Immutable
data class MainScreenState(
    val isConnected: Boolean,
    val isLoading: Boolean,
    val isDownloading: Boolean,
    val selectedStrategy: Strategy?,
    val binaryExists: Boolean,
    val error: String?,
    val connectedSince: String?,
    val strategyProgress: StrategyProgress?,
    val menuExpanded: Boolean,
) {
    companion object {
        val Initial = MainScreenState(
            isConnected = false,
            isLoading = false,
            isDownloading = false,
            selectedStrategy = null,
            binaryExists = false,
            error = null,
            connectedSince = null,
            strategyProgress = null,
            menuExpanded = false
        )
    }
}



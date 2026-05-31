package io.escaper.escaperapp.data

import io.escaper.escaperapp.domain.GetSelectedStrategyUseCase
import kotlinx.coroutines.flow.StateFlow
import io.escaper.escaperapp.domain.ProxyStartResult
import io.escaper.escaperapp.domain.ProxyStopResult
import io.escaper.escaperapp.domain.StrategiesFactory

internal expect class ProxyManager(
    pathsProvider: PathsProvider,
    downloadManager: ExecutableDownloadManager,
    getSelectedStrategy: GetSelectedStrategyUseCase,
) {
    val state: StateFlow<ProxyManagerState>

    suspend fun startProxy(): ProxyStartResult

    suspend fun stopProxy(): ProxyStopResult
}
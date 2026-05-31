package io.escaper.escaperapp.data

import io.escaper.escaperapp.domain.GetSelectedStrategyUseCase
import io.escaper.escaperapp.domain.ProxyStartResult
import io.escaper.escaperapp.domain.ProxyStopResult
import kotlinx.coroutines.flow.StateFlow

internal actual class ProxyManager actual constructor(
    pathsProvider: PathsProvider,
    downloadManager: ExecutableDownloadManager,
    getSelectedStrategy: GetSelectedStrategyUseCase,
) {
    actual val state: StateFlow<ProxyManagerState>
        get() = TODO("Not yet implemented")

    actual suspend fun startProxy(): ProxyStartResult {
        TODO("Not yet implemented")
    }

    actual suspend fun stopProxy(): ProxyStopResult {
        TODO("Not yet implemented")
    }
}
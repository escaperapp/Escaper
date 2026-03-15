package io.escaper.escaperapp.data

import kotlinx.coroutines.flow.StateFlow
import io.escaper.escaperapp.domain.ProxyStartResult
import io.escaper.escaperapp.domain.ProxyStopResult
import io.escaper.escaperapp.domain.StrategiesFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal actual class ProxyManager actual constructor(
    pathsProvider: PathsProvider,
    downloadManager: ExecutableDownloadManager,
    settingsRepository: SettingsRepository,
    strategiesFactory: StrategiesFactory,
) {
    private val _state = MutableStateFlow(ProxyManagerState.Initial)
    actual val state: StateFlow<ProxyManagerState> = _state.asStateFlow()

    actual suspend fun startProxy(): ProxyStartResult {
        TODO("Not yet implemented")
    }

    actual suspend fun stopProxy(): ProxyStopResult {
        TODO("Not yet implemented")
    }
}
package io.escaper.escaperapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import io.escaper.escaperapp.data.ExecutableDownloadManager
import io.escaper.escaperapp.data.ProxyManager
import io.escaper.escaperapp.data.SettingsRepository
import io.escaper.escaperapp.domain.ProxyStartResult
import io.escaper.escaperapp.domain.ProxyStopResult
import io.escaper.escaperapp.domain.StrategiesFactory
import io.escaper.escaperapp.domain.Strategy

private val ProxyDateTimeFormat = LocalDateTime.Format {
    hour()
    char(':')
    minute()
}

internal class MainScreenViewModel(
    private val proxyManager: ProxyManager,
    private val settingsRepository: SettingsRepository,
    private val strategiesFactory: StrategiesFactory,
    downloadManager: ExecutableDownloadManager,
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState.Initial)
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    private val _strategies = MutableStateFlow(emptyList<Strategy>())
    val strategies = _strategies.asStateFlow()

    private inline fun updateState(transform: (MainScreenState) -> MainScreenState) {
        _state.update(transform)
    }

    private fun clearError() {
        updateState { it.copy(error = null) }
    }

    private fun setError(newError: String) {
        updateState { it.copy(error = newError) }
    }

    private fun setLoading(isLoading: Boolean) {
        updateState { it.copy(isLoading = isLoading) }
    }

    init {
        viewModelScope.launch {
            _strategies.value = strategiesFactory.getStrategiesForPlatform()
        }
        downloadManager.isDownloading.onEach { isDownloading ->
            updateState {
                it.copy(isDownloading = isDownloading)
            }
        }.launchIn(viewModelScope)

        proxyManager.state.onEach { proxyState ->
            updateState {
                it.copy(
                    isConnected = proxyState.isConnected,
                    connectedSince = proxyState.connectedSince?.format(ProxyDateTimeFormat),
                )
            }
        }.launchIn(viewModelScope)

        combine(
            strategies,
            settingsRepository.observeSettings()
        ) { strategies, settings ->
            strategies.find { it.name == settings.selectedStrategy }
        }.distinctUntilChanged()
            .onEach { strategy ->
                updateState {
                    it.copy(selectedStrategy = strategy)
                }
            }.launchIn(viewModelScope)
    }

    fun setMenuExpanded(expanded: Boolean) {
        updateState {
            it.copy(menuExpanded = expanded)
        }
    }

    private var strategySelectionJob: Job? = null
    fun selectStrategy(strategy: Strategy) {
        if (strategySelectionJob?.isActive == true) return
        strategySelectionJob = viewModelScope.launch {
            settingsRepository.updateSettings {
                it.copy(selectedStrategy = strategy.name)
            }
        }
    }

    fun switchProxy() {
        if (_state.value.isConnected) {
            stopProxy()
        } else {
            startProxy()
        }
    }

    private var proxyJob: Job? = null
    fun startProxy() {
        if (proxyJob?.isActive == true) {
            return
        }
        proxyJob = viewModelScope.launch {
            setLoading(true)
            when (val result = proxyManager.startProxy()) {
                is ProxyStartResult.Error -> setError(result.errorMessage)
                is ProxyStartResult.Success -> clearError()
            }
            setLoading(false)
        }
    }

    fun stopProxy() {
        if (proxyJob?.isActive == true) {
            return
        }
        proxyJob = viewModelScope.launch {
            setLoading(true)
            when (val result = proxyManager.stopProxy()) {
                is ProxyStopResult.Error -> setError(result.errorMessage)
                ProxyStopResult.Success -> clearError()
            }
            setLoading(false)
        }
    }
}

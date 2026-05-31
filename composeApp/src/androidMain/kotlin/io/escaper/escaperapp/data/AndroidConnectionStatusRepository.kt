package io.escaper.escaperapp.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class AndroidConnectionStatusRepository {
    private val _connectionState = MutableStateFlow(ProxyManagerState.Disconnected)
    val connectionState = _connectionState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun updateState(state: ProxyManagerState) {
        _connectionState.update { state }
    }

    fun updateErrorMessage(error: String?) {
        _errorMessage.update { error }
    }
}
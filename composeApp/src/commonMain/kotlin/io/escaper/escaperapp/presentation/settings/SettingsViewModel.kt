package io.escaper.escaperapp.presentation.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class SettingsViewModel : ViewModel() {
    private val _state = MutableStateFlow(SettingsScreenState.Initial)
    val state = _state.asStateFlow()


}
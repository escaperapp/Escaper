package io.escaper.escaperapp.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.escaper.escaperapp.domain.AppLanguage
import io.escaper.escaperapp.domain.LocaleRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    private val localeRepository: LocaleRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsScreenState.Initial)
    val state = _state.asStateFlow()

    private var localeUpdateJob: Job? = null
    fun updateLocale(locale: AppLanguage) {
        if (localeUpdateJob?.isActive == true) return
        localeUpdateJob = viewModelScope.launch {
            localeRepository.updateLocale(locale)
        }
    }

    private fun subscribeToLocale() {
        viewModelScope.launch {
            localeRepository.observeLocale().collectLatest { locale ->
                _state.update { it.copy(appLanguage = locale) }
            }
        }
    }

    init {
        subscribeToLocale()
    }
}
package io.escaper.escaperapp.platform

import androidx.compose.runtime.Composable
import io.escaper.escaperapp.domain.AppLanguage
import io.escaper.escaperapp.domain.LocaleRepository
import kotlinx.coroutines.flow.Flow

internal expect fun getDefaultLocale(): AppLanguage

internal expect fun LocaleRepository.initializeLocale()

@Composable
internal expect fun ObserveLocaleUpdates(locale: Flow<AppLanguage>)

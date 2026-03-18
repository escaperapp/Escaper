package io.escaper.escaperapp.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.escaper.escaperapp.domain.AppLanguage
import io.escaper.escaperapp.domain.LocaleRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.util.Locale

internal actual fun getDefaultLocale(): AppLanguage {
    return AppLanguage.fromPlatformCode(Locale.getDefault().language)
}

@Composable
internal actual fun ObserveLocaleUpdates(locale: Flow<AppLanguage>) {
    LaunchedEffect(Unit) {
        locale.collect { lang ->
            setDesktopLocale(lang)
        }
    }
}

internal fun setDesktopLocale(lang: AppLanguage) {
    val jvmLocale = Locale.forLanguageTag(lang.isoCode)
    Locale.setDefault(jvmLocale)
}

@OptIn(DelicateCoroutinesApi::class)
internal actual fun LocaleRepository.initializeLocale() {
    setDesktopLocale(getLocaleBlocking())
}
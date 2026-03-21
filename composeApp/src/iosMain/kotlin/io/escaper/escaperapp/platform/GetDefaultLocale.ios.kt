package io.escaper.escaperapp.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.escaper.escaperapp.domain.AppLanguage
import io.escaper.escaperapp.domain.LocaleRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages

private const val LANG_KEY = "AppleLanguages"

internal actual fun getDefaultLocale(): AppLanguage {
    val isoCode = NSLocale.preferredLanguages.first() as String
    return AppLanguage.fromPlatformCode(isoCode)
}

@Composable
internal actual fun ObserveLocaleUpdates(
    locale: Flow<AppLanguage>,
) {
    LaunchedEffect(locale) {
        locale.collect { lang ->
            setIosLocale(lang)
        }
    }
}

internal fun setIosLocale(locale1: AppLanguage) {
    NSUserDefaults.standardUserDefaults.setObject(
        value = listOf(locale1.isoCode),
        forKey = LANG_KEY
    )
}

@OptIn(DelicateCoroutinesApi::class)
internal actual fun LocaleRepository.initializeLocale() {
    setIosLocale(getLocaleBlocking())
}
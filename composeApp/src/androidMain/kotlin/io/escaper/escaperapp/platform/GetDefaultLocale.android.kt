package io.escaper.escaperapp.platform

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import io.escaper.escaperapp.domain.AppLanguage
import io.escaper.escaperapp.domain.LocaleRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.util.Locale

internal actual fun getDefaultLocale(): AppLanguage {
    return AppLanguage.fromPlatformCode(Locale.getDefault().language)
}

@Composable
internal actual fun ObserveLocaleUpdates(
    locale: Flow<AppLanguage>,
) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    LaunchedEffect(locale, configuration, context) {
        locale.collect { lang ->
            val newConfig = context.updateAndroidLocale(
                locale = lang,
                cfg = configuration,
            )
            LocalConfiguration.provides(newConfig)
        }
    }
}

private fun Context.updateConfiguration(
    configuration: Configuration,
) {
    val resources = resources
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

fun Context.updateAndroidLocale(
    locale: AppLanguage,
    cfg: Configuration,
): Configuration {
    val newLocale = Locale.forLanguageTag(locale.isoCode)
    Locale.setDefault(newLocale)
    val newConfig = Configuration(cfg).apply {
        setLocale(newLocale)
    }
    this.updateConfiguration(newConfig)
    return newConfig
}

@OptIn(DelicateCoroutinesApi::class)
internal actual fun LocaleRepository.initializeLocale() {
    val localeListCompat = LocaleListCompat.forLanguageTags(
        getLocaleBlocking().isoCode
    )
    AppCompatDelegate.setApplicationLocales(localeListCompat)
}
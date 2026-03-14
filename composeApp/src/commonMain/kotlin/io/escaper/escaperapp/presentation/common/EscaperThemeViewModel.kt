package io.escaper.escaperapp.presentation.common

import androidx.compose.runtime.Composable
import io.github.themeanimator.storage.themeViewModel
import io.github.themeanimator.theme.ThemeProvider

@Composable
internal fun escaperThemeViewModel(): ThemeProvider {
    return themeViewModel(
        preferencesFileName = "escaper_theme.preferences_pb",
        jvmChildDirectory = ".escaper"
    )
}
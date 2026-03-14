package io.escaper.escaperapp.presentation.settings

import io.escaper.escaperapp.domain.AppLanguage

data class SettingsScreenState(
    val appLanguage: AppLanguage,
) {
    companion object {
        val Initial = SettingsScreenState(
            appLanguage = AppLanguage.English,
        )
    }
}
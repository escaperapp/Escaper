package io.escaper.escaperapp.navigation

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface EscaperScreen {
    @Serializable
    data object MainScreen : EscaperScreen

    @Serializable
    data object SettingsScreen : EscaperScreen

    @Serializable
    data object MyStrategiesScreen : EscaperScreen
}
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

    @Serializable
    data class EditStrategyScreen(
        val editMode: StrategyEditMode
    ) : EscaperScreen
}

@Serializable
sealed interface StrategyEditMode {
    @Serializable
    data object Create : StrategyEditMode

    @Serializable
    data class Update(
        val strategyId: String,
        val strategyName: String,
    ) : StrategyEditMode
}
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
    ) {
        @Serializable
        sealed interface StrategyEditMode {
            data object Create : StrategyEditMode
            data class Update(
                val strategyId: String,
            ) : StrategyEditMode
        }
    }
}
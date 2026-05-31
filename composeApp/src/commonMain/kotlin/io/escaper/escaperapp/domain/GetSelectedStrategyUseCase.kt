package io.escaper.escaperapp.domain

import io.escaper.escaperapp.data.SettingsRepository

internal class GetSelectedStrategyUseCase(
    private val settingsRepository: SettingsRepository,
    private val strategiesFactory: StrategiesFactory,
) {
    suspend operator fun invoke(): Strategy? {
        val allStrategies = strategiesFactory.getStrategiesForPlatform()
        val settings = settingsRepository.getSettings()

        return allStrategies.find {
            settings.selectedStrategy == it.name
        }
    }
}
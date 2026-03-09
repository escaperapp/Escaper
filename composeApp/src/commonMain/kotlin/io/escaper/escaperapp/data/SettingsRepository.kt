package io.escaper.escaperapp.data

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeSettings(): Flow<Settings>

    suspend fun getSettings(): Settings
    suspend fun updateSettings(onUpdate: (Settings) -> Settings)
}

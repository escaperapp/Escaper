package io.escaper.escaperapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

internal class DataStoreSettingsRepository(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {
    override fun observeSettings(): Flow<Settings> {
        return dataStore.data.map { prefs ->
            prefs.toSettings()
        }.distinctUntilChanged()
    }

    override suspend fun getSettings(): Settings {
        return dataStore.data.firstOrNull()?.toSettings() ?: Settings.Default
    }

    private fun Preferences.toSettings(): Settings {
        return Settings(
            autoStart = this[AutoStart] ?: false,
            autoConnect = this[AutoConnect] ?: false,
            selectedStrategy = this[SelectedStrategy],
            lastWorkingStrategy = this[LastWorkingStrategy]
        )
    }

    override suspend fun updateSettings(onUpdate: (Settings) -> Settings) {
        dataStore.edit { prefs ->
            val current = prefs.toSettings()
            val updated = onUpdate(current)

            prefs[AutoStart] = updated.autoStart
            prefs[AutoConnect] = updated.autoConnect

            if (updated.selectedStrategy != null)
                prefs[SelectedStrategy] = updated.selectedStrategy
            else
                prefs.remove(SelectedStrategy)

            if (updated.lastWorkingStrategy != null)
                prefs[LastWorkingStrategy] = updated.lastWorkingStrategy
            else
                prefs.remove(LastWorkingStrategy)
        }
    }

    private companion object Keys {
        val AutoStart = booleanPreferencesKey("auto_start")
        val AutoConnect = booleanPreferencesKey("auto_connect")
        val SelectedStrategy = stringPreferencesKey("selected_strategy")
        val LastWorkingStrategy = stringPreferencesKey("last_working_strategy")
    }
}
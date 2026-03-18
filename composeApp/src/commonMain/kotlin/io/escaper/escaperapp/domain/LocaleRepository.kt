package io.escaper.escaperapp.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.escaper.escaperapp.platform.getDefaultLocale
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

internal class LocaleRepository(
    private val dataStore: DataStore<Preferences>
) {
    private val languageKey = stringPreferencesKey(LANG_KEY)

    suspend fun getLocale(): AppLanguage {
        return dataStore.data.firstOrNull().getLanguage()
    }

    @DelicateCoroutinesApi
    fun getLocaleBlocking(): AppLanguage = runBlocking { getLocale() }

    suspend fun updateLocale(newLocale: AppLanguage) {
        dataStore.edit {
            it[languageKey] = newLocale.isoCode
        }
    }

    fun observeLocale(): Flow<AppLanguage> {
        return dataStore.data
            .map { it.getLanguage() }
            .distinctUntilChanged()
    }

    private fun Preferences?.getLanguage(): AppLanguage {
        return this?.get(languageKey)
            ?.let(AppLanguage::fromIsoCode)
            ?: getDefaultLocale()
    }

    private companion object {
        const val LANG_KEY = "ESCAPER_APP_LANGUAGE"
    }
}
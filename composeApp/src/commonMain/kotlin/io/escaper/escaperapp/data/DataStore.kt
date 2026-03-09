package io.escaper.escaperapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.scope.Scope

internal expect fun Scope.createDataStore(): DataStore<Preferences>

internal const val dataStoreFileName = "escaper.preferences_pb"
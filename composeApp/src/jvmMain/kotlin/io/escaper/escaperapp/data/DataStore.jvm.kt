package io.escaper.escaperapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toOkioPath
import org.koin.core.scope.Scope

internal actual fun Scope.createDataStore(): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            getUserDataDirPath().resolve(dataStoreFileName).toOkioPath()
        }
    )
}
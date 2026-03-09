package io.escaper.escaperapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.core.scope.Scope

internal actual fun Scope.createDataStore(): DataStore<Preferences> {
    val context: Context = get()
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            context.filesDir.resolve(dataStoreFileName)
                .absolutePath.toPath()
        }
    )
}
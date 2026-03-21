package io.escaper.escaperapp.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.scope.Scope

internal actual fun Scope.getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val appContext: Context = get()
    val dbFile = appContext.getDatabasePath(DATABASE_FILENAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
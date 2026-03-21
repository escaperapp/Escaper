package io.escaper.escaperapp.data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import io.escaper.escaperapp.data.getNsDocumentDirectory
import org.koin.core.scope.Scope

internal actual fun Scope.getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = "${getNsDocumentDirectory()}/$DATABASE_FILENAME"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
    )
}

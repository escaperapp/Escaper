package io.escaper.escaperapp.data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import io.escaper.escaperapp.data.getUserDataDirPath
import org.koin.core.scope.Scope
import kotlin.io.path.absolutePathString

internal actual fun Scope.getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = getUserDataDirPath().resolve(DATABASE_FILENAME)
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePathString(),
    )
}
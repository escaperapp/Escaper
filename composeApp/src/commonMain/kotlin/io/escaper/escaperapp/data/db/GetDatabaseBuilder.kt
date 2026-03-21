package io.escaper.escaperapp.data.db

import androidx.room.RoomDatabase
import org.koin.core.scope.Scope

internal const val DATABASE_FILENAME = "escaper_room.db"

internal expect fun Scope.getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
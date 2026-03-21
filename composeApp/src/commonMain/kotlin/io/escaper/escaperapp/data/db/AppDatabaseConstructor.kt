package io.escaper.escaperapp.data.db

import androidx.room.RoomDatabaseConstructor

@Suppress("KotlinNoActualForExpect")
internal expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
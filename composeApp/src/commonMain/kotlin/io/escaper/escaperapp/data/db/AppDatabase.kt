package io.escaper.escaperapp.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.escaper.escaperapp.data.db.daos.StrategyDao
import io.escaper.escaperapp.data.db.entities.ArgsGroupEntity
import io.escaper.escaperapp.data.db.entities.StrategyEntity
import io.escaper.escaperapp.data.db.entities.StringListConverter

@Database(
    entities = [StrategyEntity::class, ArgsGroupEntity::class],
    version = 1
)
@TypeConverters(StringListConverter::class)
@ConstructedBy(AppDatabaseConstructor::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun getStrategyDao(): StrategyDao
}
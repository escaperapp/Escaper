package io.escaper.escaperapp.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.escaper.escaperapp.data.db.entities.ArgsGroupEntity
import io.escaper.escaperapp.data.db.entities.StrategyEntity
import io.escaper.escaperapp.data.db.entities.StrategyWithGroups
import kotlinx.coroutines.flow.Flow

@Dao
internal interface StrategyDao {

    @Transaction
    @Query("SELECT * FROM strategies")
    fun subscribeToStrategies(): Flow<List<StrategyWithGroups>>

    @Transaction
    @Query("SELECT * FROM strategies")
    suspend fun getStrategies(): List<StrategyWithGroups>

    @Transaction
    @Query("SELECT * FROM strategies WHERE id = :strategyId")
    suspend fun getStrategyById(strategyId: String): StrategyWithGroups?

    @Transaction
    @Query("DELETE FROM strategies WHERE id = :strategyId")
    suspend fun deleteStrategyById(strategyId: String)

    @Transaction
    suspend fun createOrUpdateStrategy(strategy: StrategyWithGroups) {
        insertStrategy(strategy.strategy)
        for (group in strategy.groups) {
            insertArgsGroup(group)
        }
    }

    @Insert(entity = StrategyEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStrategy(strategy: StrategyEntity)

    @Insert(entity = ArgsGroupEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArgsGroup(group: ArgsGroupEntity)
}
package io.escaper.escaperapp.data.db.daos

import androidx.compose.runtime.State
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
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
}
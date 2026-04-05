package io.escaper.escaperapp.data

import io.escaper.escaperapp.data.db.AppDatabase
import io.escaper.escaperapp.data.db.entities.StrategyWithGroups
import io.escaper.escaperapp.domain.Strategy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class StrategyRepository(
    private val database: AppDatabase,
) {
    private val dao by lazy { database.getStrategyDao() }
    fun observeStrategies(): Flow<List<Strategy>> {
        return dao.subscribeToStrategies().map { strategiesWithGroups ->
            strategiesWithGroups.map { it.toStrategy() }
        }
    }

    suspend fun deleteStrategy(strategy: Strategy) {
        dao.deleteStrategyById(strategy.id)
    }

    suspend fun getStrategyById(id: String): Strategy? {
        return dao.getStrategyById(id)?.toStrategy()
    }

    private fun StrategyWithGroups.toStrategy() = Strategy(
        id = strategy.id,
        name = strategy.name,
        args = emptyList(),
        zapretArgs = groups.flatMap { it.args }
    )
}
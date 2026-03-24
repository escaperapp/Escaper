package io.escaper.escaperapp.data

import io.escaper.escaperapp.data.db.AppDatabase
import io.escaper.escaperapp.domain.Strategy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class StrategyRepository(
    private val database: AppDatabase,
) {
    private val dao by lazy { database.getStrategyDao() }
    fun observeStrategies(): Flow<List<Strategy>> {
        return dao.subscribeToStrategies().map { strategiesWithGroups ->
            strategiesWithGroups.map { strategyWithGroups ->
                Strategy(
                    name = strategyWithGroups.strategy.name,
                    args = strategyWithGroups.groups.flatMap { it.args }
                )
            }
        }
    }
}
package io.escaper.escaperapp.data

import io.escaper.escaperapp.data.db.AppDatabase
import io.escaper.escaperapp.data.db.entities.ArgsGroupEntity
import io.escaper.escaperapp.data.db.entities.StrategyEntity
import io.escaper.escaperapp.data.db.entities.StrategyWithGroups
import io.escaper.escaperapp.domain.GroupOfArguments
import io.escaper.escaperapp.domain.Strategy
import io.escaper.escaperapp.presentation.editstrategy.TempStrategyModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class StrategiesRepository(
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

    suspend fun createOrUpdateStrategy(
        strategy: TempStrategyModel,
    ) {
        dao.createOrUpdateStrategy(strategy.toDbStrategy())
    }

    private fun StrategyWithGroups.toStrategy() = Strategy(
        id = strategy.id,
        name = strategy.name,
        args = emptyList(),
        groups = groups
            .map { it.toDomainArgsGroup() }
            .sortedBy { it.indexInStrategy }
    )

    private fun TempStrategyModel.toDbStrategy(): StrategyWithGroups {
        return StrategyWithGroups(
            strategy = StrategyEntity(
                id = id,
                name = name,
            ),
            groups = groups.mapIndexed { index, arguments ->
                ArgsGroupEntity(
                    id = arguments.id,
                    strategyId = id,
                    indexInStrategy = index,
                    args = arguments.args
                )
            }
        )
    }

    private fun ArgsGroupEntity.toDomainArgsGroup() = GroupOfArguments(
        id = id,
        indexInStrategy = indexInStrategy,
        args = args
    )
}
package io.escaper.escaperapp.data.db.entities

import androidx.room.Embedded
import androidx.room.Relation

internal data class StrategyWithGroups(
    @Embedded
    val strategy: StrategyEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "strategyId"
    )
    val groups: List<ArgsGroupEntity>,
)

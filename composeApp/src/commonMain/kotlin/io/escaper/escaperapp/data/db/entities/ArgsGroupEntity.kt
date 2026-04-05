package io.escaper.escaperapp.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import io.escaper.escaperapp.domain.args.ZapretArgument

@Entity(
    tableName = "argsGroups",
    foreignKeys = [
        ForeignKey(
            entity = StrategyEntity::class,
            parentColumns = ["id"],
            childColumns = ["strategyId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index("strategyId")],
)
internal data class ArgsGroupEntity(
    @PrimaryKey
    val id: String,
    val strategyId: String,
    val indexInStrategy: Int,
    val args: List<ZapretArgument<*, *>>,
)
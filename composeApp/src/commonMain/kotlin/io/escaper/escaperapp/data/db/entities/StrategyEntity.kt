package io.escaper.escaperapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "strategies")
internal data class StrategyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
)
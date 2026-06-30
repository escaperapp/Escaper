package io.escaper.escaperapp.presentation.editstrategy

import io.escaper.escaperapp.domain.GroupOfArguments
import io.escaper.escaperapp.domain.utils.newUuid

data class TempStrategyModel(
    val id: String,
    val name: String,
    val groups: List<GroupOfArguments>,
) {
    companion object {
        val Empty
            get() = TempStrategyModel(
                id = newUuid(),
                name = "",
                groups = emptyList(),
            )
    }
}
package io.escaper.escaperapp.presentation.editstrategy

import io.escaper.escaperapp.domain.GroupOfArguments

data class TempStrategyModel(
    val name: String,
    val groups: List<GroupOfArguments>,
) {
    companion object {
        fun createEmpty() = TempStrategyModel(
            name = "",
            groups = emptyList(),
        )
    }
}
package io.escaper.escaperapp.presentation.editstrategy

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
package io.escaper.escaperapp.presentation.editstrategy

data class TempStrategyModel(
    val name: String,
) {
    companion object {
        fun createEmpty() = TempStrategyModel(
            name = ""
        )
    }
}
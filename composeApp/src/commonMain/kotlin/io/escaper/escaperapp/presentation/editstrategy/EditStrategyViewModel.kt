package io.escaper.escaperapp.presentation.editstrategy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.escaper.escaperapp.data.StrategyRepository
import io.escaper.escaperapp.navigation.StrategyEditMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class EditStrategyViewModel(
    private val editMode: StrategyEditMode,
    private val strategyRepository: StrategyRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(EditStrategyState.Initial)
    val state = _state.asStateFlow()

    private fun initializeTempStrategy() {
        viewModelScope.launch {
            val initialStrategy = when (editMode) {
                StrategyEditMode.Create -> {
                    TempStrategyModel.createEmpty()
                }

                is StrategyEditMode.Update -> {
                    val dbStrategy = strategyRepository.getStrategyById(editMode.strategyId)
                    dbStrategy?.let {
                        TempStrategyModel(
                            name = it.name,
                        )
                    } ?: TempStrategyModel.createEmpty()
                }
            }
            _state.update {
                it.copy(strategy = initialStrategy)
            }
        }
    }

    init {
        initializeTempStrategy()
    }
}

data class EditStrategyState(
    val strategy: TempStrategyModel?,
) {
    companion object {
        val Initial = EditStrategyState(
            strategy = null,
        )
    }
}
package io.escaper.escaperapp.presentation.mystrategies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.escaper.escaperapp.data.StrategiesRepository
import io.escaper.escaperapp.domain.Strategy
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.jvm.JvmInline

internal class MyStrategiesViewModel(
    private val strategiesRepository: StrategiesRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(MyStrategiesState.Initial)
    val state = _state.asStateFlow()

    init {
        subscribeToStrategies()
    }

    private fun subscribeToStrategies() {
        viewModelScope.launch {
            strategiesRepository.observeStrategies().collectLatest { strategies ->
                _state.update {
                    it.copy(strategies = strategies)
                }
            }
        }
    }

    fun onEvent(event: MyStrategiesEvent) {
        when (event) {
            is MyStrategiesEvent.DeleteStrategy -> {
                deleteStrategy(event.strategy)
            }

            is MyStrategiesEvent.ShowDeletionConfirmation -> {
                _state.update {
                    it.copy(strategyPendingForDeletion = event.strategy)
                }
            }

            MyStrategiesEvent.HideDeletionConfirmation -> {
                hidePendingConfirmation()
            }
        }
    }

    private var deleteStrategyJob: Job? = null

    private fun deleteStrategy(
        strategy: Strategy,
    ) {
        if (deleteStrategyJob?.isActive == true) {
            return
        }
        deleteStrategyJob = viewModelScope.launch {
            strategiesRepository.deleteStrategy(strategy)
            hidePendingConfirmation()
        }
    }

    private fun hidePendingConfirmation() {
        _state.update {
            it.copy(strategyPendingForDeletion = null)
        }
    }
}

internal sealed interface MyStrategiesEvent {
    @JvmInline
    value class ShowDeletionConfirmation(
        val strategy: Strategy,
    ) : MyStrategiesEvent

    object HideDeletionConfirmation : MyStrategiesEvent

    @JvmInline
    value class DeleteStrategy(
        val strategy: Strategy,
    ) : MyStrategiesEvent
}

data class MyStrategiesState(
    val strategyPendingForDeletion: Strategy?,
    val strategies: List<Strategy>
) {
    companion object {
        val Initial = MyStrategiesState(
            strategyPendingForDeletion = null,
            strategies = emptyList()
        )
    }
}
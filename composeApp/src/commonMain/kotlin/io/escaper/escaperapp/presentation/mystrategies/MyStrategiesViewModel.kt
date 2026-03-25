package io.escaper.escaperapp.presentation.mystrategies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.escaper.escaperapp.data.StrategyRepository
import io.escaper.escaperapp.domain.Strategy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class MyStrategiesViewModel(
    private val strategyRepository: StrategyRepository,
) : ViewModel() {
    private val _strategies = MutableStateFlow<List<Strategy>>(emptyList())
    val strategies = _strategies.asStateFlow()

    init {
        subscribeToStrategies()
    }

    private fun subscribeToStrategies() {
        viewModelScope.launch {
            strategyRepository.observeStrategies().collectLatest { strategies ->
                _strategies.value = strategies
            }
        }
    }

    fun deleteStrategy(strategy: Strategy) {
        // TODO: implement delete
    }
}

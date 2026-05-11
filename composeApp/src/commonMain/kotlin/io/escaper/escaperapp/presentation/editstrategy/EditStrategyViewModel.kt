package io.escaper.escaperapp.presentation.editstrategy

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.escaper.escaperapp.data.StrategiesRepository
import io.escaper.escaperapp.domain.ExecutableType
import io.escaper.escaperapp.domain.GroupOfArguments
import io.escaper.escaperapp.domain.args.AnyZapretArgument
import io.escaper.escaperapp.domain.utils.newUuid
import io.escaper.escaperapp.navigation.StrategyEditMode
import io.escaper.escaperapp.platform.PlatformProvider
import io.escaper.escaperapp.presentation.editstrategy.EditArgumentState.CreateNew
import io.escaper.escaperapp.presentation.editstrategy.EditArgumentState.EditExisting
import io.escaper.escaperapp.presentation.editstrategy.EditArgumentState.Missing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Stable
internal class EditStrategyViewModel(
    private val editMode: StrategyEditMode,
    private val strategiesRepository: StrategiesRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(EditStrategyState.Initial)
    val state = _state.asStateFlow()

    fun onEvent(event: StrategyEditEvent) {
        when (event) {
            is StrategyEditEvent.OnAddArgument -> {

            }

            is StrategyEditEvent.InitiateArgumentCreation -> {
                _state.update {
                    it.copy(
                        argumentEditState = CreateNew(event.groupIndex)
                    )
                }
            }

            is StrategyEditEvent.InitiateArgumentEditing -> {
                _state.update {
                    it.copy(
                        argumentEditState = EditExisting(
                            groupIndex = event.groupIndex,
                            argument = event.argument
                        )
                    )
                }
            }

            StrategyEditEvent.CancelArgumentEditing -> {
                _state.update {
                    it.copy(
                        argumentEditState = Missing
                    )
                }
            }

            StrategyEditEvent.AddGroup -> {
                _state.update {
                    val oldStrategy = it.strategy ?: return@update it
                    val newGroups = oldStrategy.groups.toMutableList().apply {
                        val newIndex = oldStrategy.groups.indices.lastOrNull()?.plus(1) ?: 0
                        add(
                            GroupOfArguments(
                                id = newUuid(),
                                indexInStrategy = newIndex,
                                args = emptyList()
                            )
                        )
                    }
                    it.copy(
                        strategy = oldStrategy.copy(
                            groups = newGroups
                        )
                    )
                }
            }
        }
    }

    private fun initializeTempStrategy() {
        viewModelScope.launch {
            val initialStrategy = when (editMode) {
                StrategyEditMode.Create -> {
                    TempStrategyModel.createEmpty()
                }

                is StrategyEditMode.Update -> {
                    val dbStrategy = strategiesRepository.getStrategyById(editMode.strategyId)
                    dbStrategy?.let {
                        TempStrategyModel(
                            name = it.name,
                            groups = dbStrategy.groups
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

sealed interface StrategyEditEvent {
    data class InitiateArgumentCreation(
        val groupIndex: Int,
    ) : StrategyEditEvent

    data class InitiateArgumentEditing(
        val groupIndex: Int,
        val argument: AnyZapretArgument,
    ) : StrategyEditEvent

    object CancelArgumentEditing : StrategyEditEvent

    data class OnAddArgument(
        val groupIndex: Int,
        val argument: AnyZapretArgument,
    ) : StrategyEditEvent

    object AddGroup : StrategyEditEvent
}

data class EditStrategyState(
    val strategy: TempStrategyModel?,
    val argumentEditState: EditArgumentState,
    val executableType: ExecutableType,
) {
    companion object {
        val Initial = EditStrategyState(
            strategy = null,
            argumentEditState = Missing,
            executableType = PlatformProvider.platform.executableType
        )
    }
}

sealed interface EditArgumentState {
    val isVisible: Boolean
    val groupIndex: Int

    object Missing : EditArgumentState {
        override val isVisible: Boolean = false
        override val groupIndex: Int = 0
    }

    data class CreateNew(
        override val groupIndex: Int,
    ) : EditArgumentState {
        override val isVisible: Boolean = true
    }

    data class EditExisting(
        override val groupIndex: Int,
        val argument: AnyZapretArgument,
    ) : EditArgumentState {
        override val isVisible: Boolean = true
    }
}
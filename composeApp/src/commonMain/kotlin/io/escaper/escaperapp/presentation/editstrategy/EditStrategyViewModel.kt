package io.escaper.escaperapp.presentation.editstrategy

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.unnamed_strategy
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

@Stable
internal class EditStrategyViewModel(
    private val editMode: StrategyEditMode,
    private val navController: NavController,
    private val strategiesRepository: StrategiesRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(EditStrategyState.Initial)
    val state = _state.asStateFlow()

    fun onEvent(event: StrategyEditEvent) {
        when (event) {
            is StrategyEditEvent.OnAddArgument -> {
                _state.update {
                    val oldStrategy = it.strategy
                    val newGroups = oldStrategy.groups.toMutableList().apply {
                        val oldGroup = getOrNull(event.groupIndex) ?: return@update it
                        set(event.groupIndex, oldGroup.copy(args = oldGroup.args + event.argument))
                    }
                    it.copy(
                        strategy = oldStrategy.copy(
                            groups = newGroups
                        )
                    )
                }
                cancelArgumentEditing()
            }

            is StrategyEditEvent.OnUpdateArgument -> {
                _state.update {
                    val oldStrategy = it.strategy
                    val newGroups = oldStrategy.groups.toMutableList().apply {
                        val oldGroup = getOrNull(event.groupIndex) ?: return@update it
                        set(
                            event.groupIndex,
                            oldGroup.copy(
                                args = oldGroup.args.toMutableList().apply {
                                    set(event.argumentIndex, event.argument)
                                }
                            )
                        )
                    }
                    it.copy(
                        strategy = oldStrategy.copy(
                            groups = newGroups
                        )
                    )
                }
                cancelArgumentEditing()
            }

            is StrategyEditEvent.OnEditArgument -> {
                _state.update {
                    val oldStrategy = it.strategy
                    val newGroups = oldStrategy.groups.toMutableList().apply {
                        val oldGroup = getOrNull(event.groupIndex) ?: return@update it
                        set(
                            event.groupIndex,
                            oldGroup.copy(
                                args = oldGroup.args.toMutableList().apply {
                                    set(event.argumentIndex, event.argument)
                                }
                            )
                        )
                    }
                    it.copy(
                        strategy = oldStrategy.copy(
                            groups = newGroups
                        )
                    )
                }
                cancelArgumentEditing()
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
                            argument = event.argument,
                            argumentIndex = event.argumentIndex
                        )
                    )
                }
            }

            StrategyEditEvent.CancelArgumentEditing -> {
                cancelArgumentEditing()
            }

            StrategyEditEvent.AddGroup -> {
                _state.update {
                    val oldStrategy = it.strategy
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

            is StrategyEditEvent.OnDeleteArgument -> {
                _state.update {
                    val oldStrategy = it.strategy
                    val newGroups = oldStrategy.groups.toMutableList().apply {
                        val oldGroup = getOrNull(event.groupIndex) ?: return@update it
                        set(
                            event.groupIndex,
                            oldGroup.copy(
                                args = oldGroup.args.toMutableList().apply {
                                    removeAt(event.argumentIndex)
                                }
                            )
                        )
                    }
                    it.copy(
                        strategy = oldStrategy.copy(
                            groups = newGroups
                        )
                    )
                }
                cancelArgumentEditing()
            }

            StrategyEditEvent.SaveStrategy -> saveStrategy()
            is StrategyEditEvent.EditStrategyName -> {
                _state.update {
                    val oldStrategy = it.strategy
                    it.copy(
                        strategy = oldStrategy.copy(
                            name = event.name
                        )
                    )
                }
            }
        }
    }

    private fun cancelArgumentEditing() {
        _state.update {
            it.copy(
                argumentEditState = Missing
            )
        }
    }

    private fun initializeTempStrategy() {
        viewModelScope.launch {
            val initialStrategy = when (editMode) {
                StrategyEditMode.Create -> {
                    TempStrategyModel.Empty
                }

                is StrategyEditMode.Update -> {
                    val dbStrategy = strategiesRepository.getStrategyById(editMode.strategyId)
                    dbStrategy?.let {
                        TempStrategyModel(
                            id = it.id,
                            name = it.name,
                            groups = dbStrategy.groups
                        )
                    } ?: TempStrategyModel.Empty
                }
            }
            _state.update {
                it.copy(
                    oldStrategy = initialStrategy,
                    strategy = initialStrategy
                )
            }
        }
    }

    private var saveStrategyJob: Job? = null

    private fun saveStrategy() {
        val currentState = _state.value
        if (saveStrategyJob?.isActive == true) {
            return
        }
        saveStrategyJob = viewModelScope.launch {
            strategiesRepository.createOrUpdateStrategy(
                currentState.strategy.ensureNameNotEmpty()
            )
            navController.navigateUp()
        }
    }

    private suspend fun TempStrategyModel.ensureNameNotEmpty(): TempStrategyModel {
        if (name.isNotBlank()) return this
        return copy(
            name = getString(EscaperRes.string.unnamed_strategy)
        )
    }

    private fun observeIsSaveButtonEnabled() {
        viewModelScope.launch {
            _state.map { it.oldStrategy to it.strategy }.distinctUntilChanged()
                .map { (old, new) ->
                    when (editMode) {
                        StrategyEditMode.Create -> {
                            new.groups.isNotEmpty() ||
                                    new.name.isNotBlank()
                        }

                        is StrategyEditMode.Update -> old != new
                    }
                }.collectLatest { isEnabled ->
                    _state.update {
                        it.copy(isSaveButtonEnabled = isEnabled)
                    }
                }
        }
    }

    init {
        initializeTempStrategy()
        observeIsSaveButtonEnabled()
    }
}

sealed interface StrategyEditEvent {
    data class InitiateArgumentCreation(
        val groupIndex: Int,
    ) : StrategyEditEvent

    data class InitiateArgumentEditing(
        val groupIndex: Int,
        val argumentIndex: Int,
        val argument: AnyZapretArgument,
    ) : StrategyEditEvent

    object CancelArgumentEditing : StrategyEditEvent

    data class OnAddArgument(
        val groupIndex: Int,
        val argument: AnyZapretArgument,
    ) : StrategyEditEvent

    data class OnUpdateArgument(
        val groupIndex: Int,
        val argumentIndex: Int,
        val argument: AnyZapretArgument,
    ) : StrategyEditEvent

    data class OnDeleteArgument(
        val groupIndex: Int,
        val argumentIndex: Int,
    ) : StrategyEditEvent

    data class OnEditArgument(
        val groupIndex: Int,
        val argumentIndex: Int,
        val argument: AnyZapretArgument,
    ) : StrategyEditEvent

    object AddGroup : StrategyEditEvent

    object SaveStrategy : StrategyEditEvent

    data class EditStrategyName(
        val name: String,
    ) : StrategyEditEvent
}

internal data class EditStrategyState(
    val strategy: TempStrategyModel,
    val oldStrategy: TempStrategyModel,
    val argumentEditState: EditArgumentState,
    val executableType: ExecutableType,
    val isSaveButtonEnabled: Boolean,
) {
    companion object {
        val Initial = EditStrategyState(
            strategy = TempStrategyModel.Empty,
            oldStrategy = TempStrategyModel.Empty,
            argumentEditState = Missing,
            executableType = PlatformProvider.platform.executableType,
            isSaveButtonEnabled = false,
        )
    }
}

sealed interface EditArgumentState {
    val groupIndex: Int

    object Missing : EditArgumentState {
        override val groupIndex: Int = 0
    }

    sealed interface Visible : EditArgumentState {
        val argument: AnyZapretArgument?
    }

    data class CreateNew(
        override val groupIndex: Int,
    ) : Visible {
        override val argument: AnyZapretArgument? = null
    }

    data class EditExisting(
        override val groupIndex: Int,
        val argumentIndex: Int,
        override val argument: AnyZapretArgument,
    ) : Visible
}
package io.escaper.escaperapp.presentation.argsinput

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.escaper.escaperapp.domain.ExecutableType
import io.escaper.escaperapp.domain.args.AnyZapretArgument
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.editstrategy.EditArgumentState
import io.escaper.escaperapp.presentation.editstrategy.StrategyEditEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArgumentInputSelector(
    editState: EditArgumentState,
    executableType: ExecutableType,
    onSelect: (StrategyEditEvent) -> Unit,
    onCancel: () -> Unit,
) {
    when (editState) {
        EditArgumentState.Missing -> Unit
        is EditArgumentState.Visible -> {
            val initialArgument = editState.argument
            val coroutineScope = rememberCoroutineScope()

            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
            val internalOnCancel: () -> Unit = {
                coroutineScope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    onCancel()
                }
            }
            val internalOnSelect: (AnyZapretArgument?) -> Unit = { arg ->
                coroutineScope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    when (editState) {
                        is EditArgumentState.CreateNew -> {
                            arg?.let {
                                onSelect(
                                    StrategyEditEvent.OnAddArgument(
                                        groupIndex = editState.groupIndex,
                                        argument = arg
                                    )
                                )
                            }
                        }
                        is EditArgumentState.EditExisting -> {
                            onSelect(
                                if (arg != null) {
                                    StrategyEditEvent.OnAddArgument(
                                        groupIndex = editState.groupIndex,
                                        argument = arg
                                    )
                                } else {
                                    StrategyEditEvent.OnDeleteArgument(
                                        groupIndex = editState.groupIndex,
                                        argumentIndex = editState.argumentIndex
                                    )
                                }
                            )
                        }
                    }
                }
            }

            val selectedArgumentState = rememberSaveable(
                initialArgument,
                saver = NullableArgumentState.Saver(executableType)
            ) {
                NullableArgumentState().apply {
                    initByPreselected(
                        initialArgument
                    )
                }
            }
            ModalBottomSheet(
                sheetState = sheetState,
                containerColor = EscaperTheme.background,
                contentColor = EscaperTheme.colors.mainText,
                dragHandle = {
                    BottomSheetDefaults.DragHandle(
                        color = EscaperTheme.colors.mainText
                    )
                },
                onDismissRequest = internalOnCancel
            ) {
                Column(
                    Modifier.padding(16.dp).animateContentSize()
                ) {
                    if (selectedArgumentState.preInitValue != null) {
                        ArgumentInput(
                            argumentState = selectedArgumentState,
                            onConfirmArgument = internalOnSelect,
                        )
                    } else {
                        ArgumentKeySelector(
                            executableType = executableType,
                            onSelectKey = {
                                selectedArgumentState.preInitByKey(it)
                            }
                        )
                    }
                }
            }
        }
    }
}
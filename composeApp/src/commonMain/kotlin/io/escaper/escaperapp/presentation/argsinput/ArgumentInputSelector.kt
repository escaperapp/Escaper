package io.escaper.escaperapp.presentation.argsinput

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.escaper.escaperapp.domain.ExecutableType
import io.escaper.escaperapp.domain.args.AnyZapretArgument
import io.escaper.escaperapp.domain.args.ArgumentKey
import io.escaper.escaperapp.presentation.common.EscaperTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArgumentInputSelector(
    isVisible: Boolean,
    executableType: ExecutableType,
    initialArgument: AnyZapretArgument?,
    onSelect: (AnyZapretArgument) -> Unit,
    onCancel: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var internalIsVisible by rememberSaveable {
        mutableStateOf(isVisible)
    }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(isVisible) {
        if (sheetState.isVisible && !isVisible) {
            sheetState.hide()
        }
        internalIsVisible = isVisible
    }
    val internalOnCancel: () -> Unit = {
        coroutineScope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            onCancel()
        }
    }

    var selectedKey: ArgumentKey? by rememberSaveable {
        mutableStateOf(initialArgument?.name)
    }
    if (internalIsVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            containerColor = EscaperTheme.colors.backgroundElevated,
            contentColor = EscaperTheme.colors.mainText,
            onDismissRequest = internalOnCancel
        ) {
            Column(
                Modifier.padding(16.dp)
            ) {
                selectedKey?.let { key ->
                    ArgumentInput(
                        argumentType = key,
                        executableType = executableType,
                        onSelectArgument = onSelect,
                    )
                } ?: run {
                    ArgumentKeySelector(
                        onSelectKey = {
                            selectedKey = it
                        }
                    )
                }
            }
        }
    }
}
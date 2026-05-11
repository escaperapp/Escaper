package io.escaper.escaperapp.presentation.editstrategy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.add_group_button_label
import escaper.composeapp.generated.resources.add_strategy_header
import escaper.composeapp.generated.resources.edit_strategy_header
import io.escaper.escaperapp.navigation.LocalNavController
import io.escaper.escaperapp.navigation.StrategyEditMode
import io.escaper.escaperapp.presentation.argsinput.ArgumentInputSelector
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.components.button.EscaperButton
import io.escaper.escaperapp.presentation.components.topbar.EscaperTopBar
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun EditStrategyScreen(
    mode: StrategyEditMode,
) {
    val navController = LocalNavController.current
    val viewModel: EditStrategyViewModel = koinViewModel {
        parametersOf(mode)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    EditStrategyContent(
        mode = mode,
        state = state,
        onBack = navController::navigateUp,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun EditStrategyContent(
    mode: StrategyEditMode,
    state: EditStrategyState,
    onEvent: (StrategyEditEvent) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            EscaperTopBar(
                title = mode.toLabel(),
                onBackClick = onBack
            )
        },
        contentColor = EscaperTheme.colors.mainText,
        containerColor = EscaperTheme.background
    ) { paddings ->
        LazyColumn(
            modifier = Modifier.padding(paddings),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(
                items = state.strategy?.groups.orEmpty(),
                key = { _, group -> group.hashCode() },
            ) { index, group ->

            }
            item {
                AddGroupButton(
                    onClick = {

                    }
                )
            }
        }
    }

    val editState = state.argumentEditState
    ArgumentInputSelector(
        isVisible = state.argumentEditState.isVisible,
        executableType = state.executableType,
        initialArgument = (editState as? EditArgumentState.EditExisting)?.argument,
        onSelect = { argument ->
            onEvent(
                StrategyEditEvent.OnAddArgument(
                    groupIndex = editState.groupIndex,
                    argument = argument
                )
            )
        }
    )
}

@Composable
private fun StrategyEditMode.toLabel(): String {
    return when (this) {
        StrategyEditMode.Create -> stringResource(EscaperRes.string.add_strategy_header)
        is StrategyEditMode.Update -> stringResource(
            EscaperRes.string.edit_strategy_header,
            strategyName
        )
    }
}

@Composable
private fun AddNewGroupInput() {

}

@Composable
private fun AddGroupButton(
    onClick: () -> Unit,
) {
    EscaperButton(
        title = stringResource(EscaperRes.string.add_group_button_label),
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    )
}

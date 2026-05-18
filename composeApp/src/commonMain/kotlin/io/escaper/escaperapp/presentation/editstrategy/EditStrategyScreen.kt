package io.escaper.escaperapp.presentation.editstrategy

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.add_group_button_label
import escaper.composeapp.generated.resources.add_strategy_header
import escaper.composeapp.generated.resources.edit_strategy_header
import io.escaper.escaperapp.domain.args.AnyZapretArgument
import io.escaper.escaperapp.navigation.LocalNavController
import io.escaper.escaperapp.navigation.StrategyEditMode
import io.escaper.escaperapp.presentation.argsinput.ArgumentInputSelector
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.components.button.EscaperButton
import io.escaper.escaperapp.presentation.components.topbar.EscaperTopBar
import io.escaper.escaperapp.presentation.icons.IconAdd
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
                NewGroupInput(
                    arguments = group.args,
                    onEditArgument = { argIndex, argument ->
                        onEvent(
                            StrategyEditEvent.InitiateArgumentEditing(
                                groupIndex = index,
                                argumentIndex = argIndex,
                                argument = argument
                            )
                        )
                    },
                    onAddArgument = {
                        onEvent(StrategyEditEvent.InitiateArgumentCreation(index))
                    }
                )
            }
            item {
                AddGroupButton(
                    onClick = {
                        onEvent(StrategyEditEvent.AddGroup)
                    }
                )
            }
        }
    }

    val editState = state.argumentEditState
    ArgumentInputSelector(
        editState = editState,
        executableType = state.executableType,
        onSelect = onEvent,
        onCancel = {
            onEvent(StrategyEditEvent.CancelArgumentEditing)
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
private fun NewGroupInput(
    arguments: List<AnyZapretArgument>,
    onEditArgument: (Int, AnyZapretArgument) -> Unit,
    onAddArgument: () -> Unit,
) {
    val shape = RoundedCornerShape(20.dp)
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .border(
                width = 1.dp,
                color = EscaperTheme.colors.mainText,
                shape = shape
            )
            .clip(shape)
            .background(EscaperTheme.colors.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        arguments.forEachIndexed { index, argument ->
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .clip(RoundedCornerShape(50))
                    .background(EscaperTheme.colors.backgroundElevated)
                    .clickable {
                        onEditArgument(index, argument)
                    }
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = argument.asStringArg(),
                    style = EscaperTheme.typography.bodyMedium,
                    color = EscaperTheme.colors.mainText,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        }
        Row(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .background(EscaperTheme.colors.backgroundElevated)
                .clickable(
                    onClick = onAddArgument
                )
                .padding( 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = IconAdd,
                contentDescription = "Add new group",
                modifier = Modifier.size(24.dp),
                tint = EscaperTheme.colors.mainText
            )
        }
    }
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
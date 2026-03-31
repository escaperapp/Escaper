package io.escaper.escaperapp.presentation.mystrategies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.add_strategy
import escaper.composeapp.generated.resources.my_strategies_label
import escaper.composeapp.generated.resources.no_custom_strategies_hint
import io.escaper.escaperapp.domain.Strategy
import io.escaper.escaperapp.navigation.EscaperScreen
import io.escaper.escaperapp.navigation.LocalNavController
import io.escaper.escaperapp.navigation.StrategyEditMode
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.components.topbar.EscaperTopBar
import io.escaper.escaperapp.presentation.icons.IconDelete
import io.escaper.escaperapp.presentation.icons.IconEdit
import io.escaper.escaperapp.presentation.icons.IconNoResults
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MyStrategiesScreen(
    viewModel: MyStrategiesViewModel = koinViewModel()
) {
    val navController = LocalNavController.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    MyStrategiesScreenContent(
        state = state,
        onBack = navController::navigateUp,
        onEditClick = {
            navController.navigate(
                EscaperScreen.EditStrategyScreen(
                    StrategyEditMode.Update(
                        strategyId = it.id,
                        strategyName = it.name
                    )
                )
            )
        },
        onAddStrategy = {
            navController.navigate(
                EscaperScreen.EditStrategyScreen(
                    StrategyEditMode.Create
                )
            )
        },
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun MyStrategiesScreenContent(
    state: MyStrategiesState,
    onBack: () -> Unit,
    onAddStrategy: () -> Unit,
    onEditClick: (Strategy) -> Unit,
    onEvent: (MyStrategiesEvent) -> Unit,
) {
    val strategies = state.strategies
    Scaffold(
        topBar = {
            EscaperTopBar(
                title = stringResource(EscaperRes.string.my_strategies_label),
                onBackClick = onBack
            )
        },
        contentColor = EscaperTheme.colors.mainText,
        containerColor = EscaperTheme.background
    ) { paddings ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (strategies.isEmpty()) {
                item {
                    EmptyStrategiesContent(
                        onAddStrategyClick = onAddStrategy,
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(paddings)
                    )
                }
            }
            items(
                items = strategies,
                key = { it.name }
            ) { strategy ->
                StrategyItem(
                    strategy = strategy,
                    onEditClick = { onEditClick(strategy) },
                    onDeleteClick = {
                        onEvent(MyStrategiesEvent.ShowDeletionConfirmation(strategy))
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyStrategiesContent(
    modifier: Modifier = Modifier,
    onAddStrategyClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = IconNoResults,
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                tint = EscaperTheme.colors.mainText.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(EscaperRes.string.no_custom_strategies_hint),
                color = EscaperTheme.colors.mainText.copy(alpha = 0.7f),
                style = EscaperTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onAddStrategyClick,
                modifier = Modifier.fillMaxWidth(0.6f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EscaperTheme.colors.mainButtonLight,
                    contentColor = EscaperTheme.colors.mainText
                )
            ) {
                Text(
                    text = stringResource(EscaperRes.string.add_strategy),
                    style = EscaperTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun StrategyItem(
    strategy: Strategy,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(EscaperTheme.colors.backgroundElevated),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = strategy.name,
            color = EscaperTheme.colors.mainText,
            style = EscaperTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 10.dp)
        )
        Row {
            IconButton(
                onClick = onEditClick
            ) {
                Icon(
                    imageVector = IconEdit,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = EscaperTheme.colors.mainText
                )
            }
            IconButton(
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = IconDelete,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = EscaperTheme.colors.error
                )
            }
        }
    }
}

@Preview
@Composable
private fun MyStrategiesContent() {
    EscaperTheme(
        isDark = true
    ) {
        MyStrategiesScreenContent(
            state = MyStrategiesState(
                strategyPendingForDeletion = null,
                strategies = listOf(
                    Strategy(
                        id = "123",
                        name = "Custom",
                        args = emptyList()
                    ),
                    Strategy(
                        id = "124",
                        name = "Custom 2",
                        args = emptyList()
                    )
                )
            ),
            onBack = {},
            onEditClick = {},
            onEvent = {},
            onAddStrategy = {}
        )
    }
}
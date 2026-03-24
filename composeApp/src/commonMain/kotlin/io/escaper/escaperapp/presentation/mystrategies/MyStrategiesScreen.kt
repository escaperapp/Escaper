package io.escaper.escaperapp.presentation.mystrategies

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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.escaper.escaperapp.domain.Strategy
import io.escaper.escaperapp.navigation.LocalNavController
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.components.topbar.EscaperTopBar
import io.escaper.escaperapp.presentation.icons.IconDelete
import io.escaper.escaperapp.presentation.icons.IconEdit
import io.escaper.escaperapp.presentation.icons.IconNoResults
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.add_strategy
import escaper.composeapp.generated.resources.my_strategies_label
import escaper.composeapp.generated.resources.no_custom_strategies_hint

@Composable
internal fun MyStrategiesScreen(
    viewModel: MyStrategiesViewModel = koinViewModel()
) {
    val navController = LocalNavController.current
    val strategies by viewModel.strategies.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            EscaperTopBar(
                title = stringResource(EscaperRes.string.my_strategies_label),
                onBackClick = navController::navigateUp
            )
        },
        contentColor = EscaperTheme.colors.mainText,
        containerColor = EscaperTheme.background
    ) { paddings ->
        if (strategies.isEmpty()) {
            EmptyStrategiesContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = strategies,
                    key = { it.name }
                ) { strategy ->
                    StrategyItem(
                        strategy = strategy,
                        onEditClick = { /* TODO */ },
                        onDeleteClick = { viewModel.deleteStrategy(strategy) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyStrategiesContent(
    modifier: Modifier = Modifier
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
                style = EscaperTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text(
                    text = stringResource(EscaperRes.string.add_strategy),
                    color = EscaperTheme.colors.background
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = strategy.name,
            color = EscaperTheme.colors.mainText,
            style = EscaperTheme.typography.bodyMedium
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
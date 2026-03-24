package io.escaper.escaperapp.presentation.mystrategies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.my_strategies_label

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = strategies,
                key = { it.id }
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
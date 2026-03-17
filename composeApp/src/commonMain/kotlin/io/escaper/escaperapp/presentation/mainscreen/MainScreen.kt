package io.escaper.escaperapp.presentation.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.connected_state_label
import escaper.composeapp.generated.resources.disconnected_state_label
import escaper.composeapp.generated.resources.download_in_progress_label
import escaper.composeapp.generated.resources.strategy_placeholder
import io.escaper.escaperapp.domain.Strategy
import io.escaper.escaperapp.navigation.EscaperScreen
import io.escaper.escaperapp.navigation.LocalNavController
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.components.dropdown.EscaperDropdown
import io.escaper.escaperapp.presentation.components.mainbutton.OnOffButton
import io.escaper.escaperapp.presentation.components.topbar.EscaperTopBar
import io.escaper.escaperapp.presentation.icons.IconSettings
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MainScreen(
    state: MainScreenState,
    strategies: List<Strategy>,
    onSelectStrategy: (Strategy) -> Unit,
    onMenuExpandedChange: (Boolean) -> Unit,
    onSwitchProxy: () -> Unit,
) {
    val navController = LocalNavController.current
    Scaffold(
        contentColor = EscaperTheme.colors.mainText,
        containerColor = EscaperTheme.colors.background,
        topBar = {
            EscaperTopBar(
                title = {},
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(EscaperScreen.SettingsScreen) {
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = IconSettings,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = EscaperTheme.colors.mainText
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .background(EscaperTheme.background)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(
                    if (state.isConnected) {
                        EscaperRes.string.connected_state_label
                    } else {
                        EscaperRes.string.disconnected_state_label
                    }
                ),
                style = EscaperTheme.typography.headlineLarge,
                color = EscaperTheme.colors.mainText
            )
            Spacer(Modifier.height(24.dp))
            if (state.isDownloading) {
                Text(
                    text = stringResource(EscaperRes.string.download_in_progress_label),
                    color = EscaperTheme.colors.mainText
                )
            }
            state.error?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(top = 16.dp),
                    color = EscaperTheme.colors.error
                )
            }
            Spacer(Modifier.height(48.dp))

            EscaperDropdown(
                isExpanded = state.menuExpanded,
                selectedItem = state.selectedStrategy,
                isConnected = state.isConnected,
                items = strategies,
                modifier = Modifier.padding(horizontal = 36.dp),
                emptyPlaceholder = stringResource(EscaperRes.string.strategy_placeholder),
                onSelectItem = onSelectStrategy,
                onFormatItem = { it.name },
                onExpandedChange = onMenuExpandedChange
            )
            OnOffButton(
                isConnected = state.isConnected,
                isLoading = state.isLoading,
                modifier = Modifier.padding(
                    vertical = 64.dp,
                    horizontal = 120.dp
                ),
                onClick = onSwitchProxy
            )
        }
    }
}
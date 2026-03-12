package io.escaper.escaperapp.presentation.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.escaper.escaperapp.domain.Strategy
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.components.dropdown.EscaperDropdown
import io.escaper.escaperapp.presentation.components.mainbutton.OnOffButton

@Composable
internal fun MainScreen(
    state: MainScreenState,
    strategies: List<Strategy>,
    onSelectStrategy: (Strategy) -> Unit,
    onMenuExpandedChange: (Boolean) -> Unit,
    onSwitchProxy: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(EscaperTheme.background)
            .safeContentPadding()
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = if (state.isConnected) {
                "Connected"
            } else {
                "Not connected"
            },
            style = EscaperTheme.typography.headlineLarge,
            color = EscaperTheme.colors.mainText
        )
        Spacer(Modifier.height(24.dp))
        if (state.isDownloading) {
            Text(
                text = "Binary not found. Downloading...",
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
            emptyPlaceholder = "Select a strategy",
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
package io.escaper.escaperapp

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.escaper.escaperapp.platform.UpdateWindowBackground
import io.escaper.escaperapp.presentation.MainScreenViewModel
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.components.dropdown.EscaperDropdown
import io.escaper.escaperapp.presentation.components.mainbutton.OnOffButton
import org.koin.compose.viewmodel.koinViewModel

internal const val APP_NAME = "Escaper"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val viewModel: MainScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val strategies by viewModel.strategies.collectAsStateWithLifecycle()
    val background = EscaperTheme.colorScheme.connectedAwareBackground(state.isConnected)

    EscaperTheme {
        Column(
            modifier = Modifier
                .background(background)
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
                color = EscaperTheme.colorScheme.mainText
            )
            Spacer(Modifier.height(24.dp))
            if (state.isDownloading) {
                Text(text = "Binary not found. Downloading...")
            }
            state.error?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.error
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
                onSelectItem = {
                    viewModel.selectStrategy(it)
                },
                onFormatItem = { it.name },
                onExpandedChange = {
                    viewModel.setMenuExpanded(it)
                }
            )
            OnOffButton(
                isConnected = state.isConnected,
                isLoading = state.isLoading,
                modifier = Modifier.padding(
                    vertical = 64.dp,
                    horizontal = 120.dp
                ),
                onClick = {
                    viewModel.switchProxy()
                }
            )
        }
        UpdateWindowBackground(background)
    }
}
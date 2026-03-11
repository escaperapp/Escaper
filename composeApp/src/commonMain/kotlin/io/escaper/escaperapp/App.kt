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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.escaper.escaperapp.navigation.EscaperScreen
import io.escaper.escaperapp.platform.UpdateWindowBackground
import io.escaper.escaperapp.presentation.mainscreen.MainScreenViewModel
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.components.dropdown.EscaperDropdown
import io.escaper.escaperapp.presentation.components.mainbutton.OnOffButton
import io.escaper.escaperapp.presentation.mainscreen.MainScreen
import org.koin.compose.viewmodel.koinViewModel

internal const val APP_NAME = "Escaper"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val viewModel: MainScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val strategies by viewModel.strategies.collectAsStateWithLifecycle()

    val navController = rememberNavController()

    EscaperTheme(state.isConnected) {
        NavHost(
            navController = navController,
            startDestination = EscaperScreen.MainScreen
        ) {
            composable<EscaperScreen.MainScreen> {
                MainScreen(
                    state = state,
                    strategies = strategies,
                    onSelectStrategy = viewModel::selectStrategy,
                    onSwitchProxy = viewModel::switchProxy,
                    onMenuExpandedChange = viewModel::setMenuExpanded
                )
            }
        }
        UpdateWindowBackground(EscaperTheme.background)
    }
}
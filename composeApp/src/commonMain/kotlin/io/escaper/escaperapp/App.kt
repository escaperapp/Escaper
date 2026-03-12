package io.escaper.escaperapp

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.escaper.escaperapp.navigation.EscaperScreen
import io.escaper.escaperapp.navigation.ProvideNavController
import io.escaper.escaperapp.platform.UpdateWindowBackground
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.mainscreen.MainScreen
import io.escaper.escaperapp.presentation.mainscreen.MainScreenViewModel
import io.escaper.escaperapp.presentation.settings.SettingsScreen
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
        ProvideNavController(navController) {
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
                composable<EscaperScreen.SettingsScreen> {
                    SettingsScreen()
                }
            }
        }
        UpdateWindowBackground(EscaperTheme.background)
    }
}
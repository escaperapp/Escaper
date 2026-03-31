package io.escaper.escaperapp

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.escaper.escaperapp.navigation.EscaperScreen
import io.escaper.escaperapp.navigation.ProvideNavController
import io.escaper.escaperapp.navigation.StrategyEditModeTypeMap
import io.escaper.escaperapp.platform.UpdateWindowBackground
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.common.escaperThemeViewModel
import io.escaper.escaperapp.presentation.mainscreen.MainScreen
import io.escaper.escaperapp.presentation.mainscreen.MainScreenViewModel
import io.escaper.escaperapp.presentation.mystrategies.MyStrategiesScreen
import io.escaper.escaperapp.presentation.settings.SettingsScreen
import io.github.themeanimator.theme.isDark
import org.koin.compose.viewmodel.koinViewModel

internal const val APP_NAME = "Escaper"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val viewModel: MainScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val strategies by viewModel.strategies.collectAsStateWithLifecycle()

    val navController = rememberNavController()

    val themeViewModel = escaperThemeViewModel()
    val theme by themeViewModel.currentTheme.collectAsStateWithLifecycle()

    EscaperTheme(
        isConnected = state.isConnected,
        isDark = theme.isDark()
    ) {
        ProvideNavController(navController) {
            NavHost(
                navController = navController,
                startDestination = EscaperScreen.MainScreen,
                modifier = Modifier
                    .background(EscaperTheme.background)
                    .fillMaxSize(),
                enterTransition = {
                    slideIntoContainer(
                        towards = SlideDirection.Left,
                        animationSpec = tween(600)
                    )
                },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = SlideDirection.Right,
                        animationSpec = tween(600)
                    )
                }
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
                composable<EscaperScreen.MyStrategiesScreen> {
                    MyStrategiesScreen()
                }
                composable<EscaperScreen.EditStrategyScreen>(
                    typeMap = StrategyEditModeTypeMap
                ) {
                    Text("Edit Strategy Screen")
                }
            }
        }
        UpdateWindowBackground(EscaperTheme.background)
    }
}
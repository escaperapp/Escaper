package io.escaper.escaperapp.presentation.editstrategy

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.add_strategy_header
import escaper.composeapp.generated.resources.edit_strategy_header
import io.escaper.escaperapp.navigation.LocalNavController
import io.escaper.escaperapp.navigation.StrategyEditMode
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.components.topbar.EscaperTopBar
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
    EditStrategyContent(
        mode = mode,
        onBack = navController::navigateUp
    )
}

@Composable
private fun EditStrategyContent(
    mode: StrategyEditMode,
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

    }
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
private fun AddNewGroupInput() {

}

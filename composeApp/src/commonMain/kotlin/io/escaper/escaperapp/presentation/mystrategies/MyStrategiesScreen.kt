package io.escaper.escaperapp.presentation.mystrategies

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.escaper.escaperapp.navigation.LocalNavController
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.components.topbar.EscaperTopBar
import org.jetbrains.compose.resources.stringResource
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.my_strategies_label

@Composable
internal fun MyStrategiesScreen() {
    val navController = LocalNavController.current
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
        ) {
        }
    }
}

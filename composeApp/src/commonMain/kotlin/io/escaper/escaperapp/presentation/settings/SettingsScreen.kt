package io.escaper.escaperapp.presentation.settings

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.dark_ui_theme_label
import escaper.composeapp.generated.resources.light_ui_theme_label
import escaper.composeapp.generated.resources.selected_ui_theme
import escaper.composeapp.generated.resources.settings_label
import escaper.composeapp.generated.resources.system_ui_theme_label
import io.escaper.escaperapp.navigation.LocalNavController
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.common.escaperThemeViewModel
import io.escaper.escaperapp.presentation.components.topbar.EscaperTopBar
import io.github.themeanimator.ThemeAnimationFormat
import io.github.themeanimator.ThemeAnimationScope
import io.github.themeanimator.button.ThemeSwitch
import io.github.themeanimator.button.rememberLottieIconJson
import io.github.themeanimator.rememberThemeAnimationState
import io.github.themeanimator.theme.Theme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun SettingsScreen() {
    val viewModel: SettingsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val themeViewModel = escaperThemeViewModel()
    val theme by themeViewModel.currentTheme.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    val animationSpec: AnimationSpec<Float> = tween(600)
    val themeAnimationState = rememberThemeAnimationState(
        themeProvider = themeViewModel,
        animationSpec = animationSpec,
        format = ThemeAnimationFormat.CircularAroundPress,
    )

    ThemeAnimationScope(
        state = themeAnimationState
    ) {
        Scaffold(
            topBar = {
                EscaperTopBar(
                    title = stringResource(EscaperRes.string.settings_label),
                    onBackClick = navController::navigateUp
                )
            },
            contentColor = EscaperTheme.colors.mainText,
            containerColor = EscaperTheme.background
        ) { paddings ->
            Column(
                modifier = Modifier
                    .background(EscaperTheme.background)
                    .padding(paddings)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                SettingsRow(
                    label = stringResource(
                        EscaperRes.string.selected_ui_theme,
                        stringResource(theme.toLabel())
                    ),
                    modifier = Modifier.padding(top = 36.dp)
                ) {
                    ThemeSwitch(
                        animationState = themeAnimationState,
                        iconTint = Color.Unspecified,
                        buttonIcon = rememberLottieIconJson(
                            animationSpec = animationSpec,
                            lightThemeProgress = 0f,
                            systemThemeProgress = 0.2f,
                            darkThemeProgress = 1f
                        ) {
                            EscaperRes.readBytes("files/theme_change.json").decodeToString()
                        },
                        modifier = Modifier.size(width = 72.dp, height = 36.dp),
                        iconSize = DpSize(width = 72.dp, height = 36.dp)
                    )
                }
            }
        }
    }
}

private fun Theme.toLabel(): StringResource = when (this) {
    Theme.Dark -> EscaperRes.string.dark_ui_theme_label
    Theme.Light -> EscaperRes.string.light_ui_theme_label
    Theme.System -> EscaperRes.string.system_ui_theme_label
}

@Composable
private inline fun SettingsRow(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        content = {
            Text(
                text = label,
                style = EscaperTheme.typography.bodySmall,
                color = EscaperTheme.colors.mainText,
                fontSize = 20.sp
            )
            content()
        }
    )
}
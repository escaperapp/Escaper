package io.escaper.escaperapp.presentation.settings

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import escaper.composeapp.generated.resources.EscaperRes
import io.escaper.escaperapp.navigation.LocalNavController
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.common.escaperThemeViewModel
import io.escaper.escaperapp.presentation.components.topbar.EscaperTopBar
import io.github.themeanimator.ThemeAnimationFormat
import io.github.themeanimator.ThemeAnimationScope
import io.github.themeanimator.button.ThemeSwitchButton
import io.github.themeanimator.button.rememberLottieIconJson
import io.github.themeanimator.rememberThemeAnimationState
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun SettingsScreen() {
    val viewModel: SettingsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val themeViewModel = escaperThemeViewModel()
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
                    title = "Settings",
                    onBackClick = navController::navigateUp
                )
            },
            contentColor = EscaperTheme.colors.mainText,
            containerColor = EscaperTheme.background
        ) { paddings ->
            Column(
                modifier = Modifier
                    .padding(paddings)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                SettingsRow(
                    label = "App theme",
                    modifier = Modifier.padding(top = 36.dp)
                ) {
                    ThemeSwitchButton(
                        animationState = themeAnimationState,
                        iconTint = EscaperTheme.colors.mainText,
                        buttonIcon = rememberLottieIconJson(
                            animationSpec = animationSpec,
                            lightThemeProgress = 0.8f,
                            darkThemeProgress = 0.5f
                        ) {
                            EscaperRes.readBytes("files/anim.json").decodeToString()
                        },
                        iconSize = 40.dp,
                        iconScale = 2f
                    )
                }
            }
        }
    }
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
package io.escaper.escaperapp.presentation.settings

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.dark_ui_theme_label
import escaper.composeapp.generated.resources.lang_en
import escaper.composeapp.generated.resources.lang_ru
import escaper.composeapp.generated.resources.light_ui_theme_label
import escaper.composeapp.generated.resources.my_strategies_label
import escaper.composeapp.generated.resources.selected_language
import escaper.composeapp.generated.resources.selected_ui_theme
import escaper.composeapp.generated.resources.settings_label
import escaper.composeapp.generated.resources.system_ui_theme_label
import io.escaper.escaperapp.domain.AppLanguage
import io.escaper.escaperapp.navigation.EscaperScreen
import io.escaper.escaperapp.navigation.LocalNavController
import io.escaper.escaperapp.platform.ObserveLocaleUpdates
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.common.escaperThemeViewModel
import io.escaper.escaperapp.presentation.components.topbar.EscaperTopBar
import io.escaper.escaperapp.presentation.icons.IconArrowLeft
import io.github.themeanimator.ThemeAnimationFormat
import io.github.themeanimator.ThemeAnimationScope
import io.github.themeanimator.button.ExperimentalThemeSwitchApi
import io.github.themeanimator.button.ThemeSwitch
import io.github.themeanimator.button.rememberLottieIconJson
import io.github.themeanimator.rememberThemeAnimationState
import io.github.themeanimator.theme.Theme
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private val SwitchIndication = ripple(color = Color.Black)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalThemeSwitchApi::class)
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

    ReactToLocaleChanges(viewModel.state)

    ThemeAnimationScope(
        state = themeAnimationState
    ) {
        key(state.appLanguage) {
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
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SettingsRow(
                        label = stringResource(
                            EscaperRes.string.selected_ui_theme,
                            stringResource(theme.toLabel())
                        )
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
                            iconSize = DpSize(width = 72.dp, height = 36.dp),
                            indication = SwitchIndication,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                    }
                    SettingsRow(
                        label = stringResource(
                            EscaperRes.string.selected_language
                        )
                    ) {
                        var isExpanded by rememberSaveable { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = isExpanded,
                            onExpandedChange = { isExpanded = it }
                        ) {
                            LocaleText(
                                locale = state.appLanguage,
                                modifier = Modifier.menuAnchor(
                                    ExposedDropdownMenuAnchorType.PrimaryEditable
                                ),
                            ) {
                                isExpanded = true
                            }
                            ExposedDropdownMenu(
                                expanded = isExpanded,
                                containerColor = EscaperTheme.colors.innerShadow,
                                modifier = Modifier.width(IntrinsicSize.Min),
                                onDismissRequest = { isExpanded = false }
                            ) {
                                AppLanguage.entries.forEach { lang ->
                                    LocaleText(lang, Modifier.fillMaxWidth()) {
                                        viewModel.updateLocale(lang)
                                        isExpanded = false
                                    }
                                }
                            }
                        }
                    }
                    SettingsRow(
                        label = stringResource(EscaperRes.string.my_strategies_label),
                        onClick = {
                            navController.navigate(EscaperScreen.MyStrategiesScreen) {
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = IconArrowLeft,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .rotate(180f),
                            tint = EscaperTheme.colors.mainText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LocaleText(
    locale: AppLanguage,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Text(
        text = stringResource(locale.toLabel()),
        color = EscaperTheme.colors.mainText,
        style = EscaperTheme.typography.labelLarge,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp
            )
    )
}

private fun AppLanguage.toLabel(): StringResource = when (this) {
    AppLanguage.English -> EscaperRes.string.lang_en
    AppLanguage.Russian -> EscaperRes.string.lang_ru
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
    noinline onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onClick)
                } else {
                    Modifier
                }
            ).padding(horizontal = 16.dp, vertical = 12.dp),
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

@Composable
private fun ReactToLocaleChanges(
    locale: StateFlow<SettingsScreenState>,
) {
    ObserveLocaleUpdates(
        locale.map { it.appLanguage }
            .distinctUntilChanged()
    )
}
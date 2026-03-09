package io.escaper.escaperapp.presentation.common

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Typography
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalEscaperScheme = staticCompositionLocalOf {
    DarkEscaperColorScheme
}

internal val LocalEscaperTypography = staticCompositionLocalOf {
    Typography()
}

private const val TextSelectionBackgroundOpacity = 0.4f

object EscaperTheme {
    val colorScheme: EscaperColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalEscaperScheme.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalEscaperTypography.current
}

@Composable
internal fun EscaperTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isDark) {
        DarkEscaperColorScheme
    } else {
        LightEscaperColorScheme
    }
    val rippleIndication = ripple()
    val selectionColors = rememberTextSelectionColors(colorScheme)
    val typography = EscaperTheme.typography
    CompositionLocalProvider(
        LocalEscaperScheme provides colorScheme,
        LocalIndication provides rippleIndication,
        LocalTextSelectionColors provides selectionColors,
    ) {
        ProvideTextStyle(value = typography.bodyLarge, content = content)
    }
}

@Composable
private fun rememberTextSelectionColors(
    scheme: EscaperColorScheme,
): TextSelectionColors {
    val primaryColor = scheme.mainText
    return remember(primaryColor) {
        TextSelectionColors(
            handleColor = primaryColor,
            backgroundColor = primaryColor.copy(alpha = TextSelectionBackgroundOpacity),
        )
    }
}

@Immutable
data class EscaperColorScheme(
    val background: Color,
    val backgroundDisconnected: Color,
    val mainText: Color,
    val mainButtonDark: Color,
    val mainButtonLight: Color,
    val mainButtonDisconnected: Color,
    val innerShadow: Color,
    val pingCircles: Color,
    val shadowGlow: Color,
) {
    fun connectedAwareBackground(
        isConnected: Boolean,
    ) = if (isConnected) background else backgroundDisconnected
}

private val DarkEscaperColorScheme = EscaperColorScheme(
    background = Color(34, 21, 111),
    mainText = Color.White,
    mainButtonDark = Color(103, 79, 247),
    mainButtonLight = Color(129, 108, 255),
    mainButtonDisconnected = Color(59, 58, 72),
    innerShadow = Color(39, 38, 52, 255),
    backgroundDisconnected = Color(68, 66, 82),
    pingCircles = Color(6, 66, 103),
    shadowGlow = Color(79, 214, 247),
)

// TODO: Adapt to light colors
private val LightEscaperColorScheme = EscaperColorScheme(
    background = Color(34, 21, 111),
    mainText = Color.White,
    mainButtonDark = Color(103, 79, 247),
    mainButtonLight = Color(129, 108, 255),
    mainButtonDisconnected = Color(59, 58, 72),
    innerShadow = Color(39, 38, 52, 255),
    backgroundDisconnected = Color(68, 66, 82),
    pingCircles = Color(6, 66, 103),
    shadowGlow = Color(79, 214, 247),
)
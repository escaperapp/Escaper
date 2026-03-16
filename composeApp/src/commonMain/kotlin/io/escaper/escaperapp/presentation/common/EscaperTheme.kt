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
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalEscaperScheme = staticCompositionLocalOf {
    DarkEscaperColorScheme
}

internal val LocalEscaperTypography = staticCompositionLocalOf {
    Typography()
}

internal val LocalBackgroundColor = compositionLocalOf {
    Color.Unspecified
}

private const val TextSelectionBackgroundOpacity = 0.4f

object EscaperTheme {
    val colors: EscaperColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalEscaperScheme.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalEscaperTypography.current

    val background: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalBackgroundColor.current
}

@Composable
internal fun EscaperTheme(
    isConnected: Boolean,
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isDark) {
        DarkEscaperColorScheme
    } else {
        LightEscaperColorScheme
    }
    val rippleIndication = ripple(
        color = colorScheme.mainText
    )
    val selectionColors = rememberTextSelectionColors(colorScheme)
    val typography = EscaperTheme.typography
    CompositionLocalProvider(
        LocalEscaperScheme provides colorScheme,
        LocalIndication provides rippleIndication,
        LocalTextSelectionColors provides selectionColors,
        LocalBackgroundColor provides colorScheme.connectedAwareBackground(isConnected)
    ) {
        ProvideTextStyle(
            value = typography.bodyLarge,
            content = content
        )
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
    val error: Color,
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
    error = Color(252, 140, 125, 255),
)

// TODO: Adapt to light colors
private val LightEscaperColorScheme = EscaperColorScheme(
    background = Color(225, 224, 255, 255),
    mainText = Color(33, 33, 33, 255),
    mainButtonDark = Color(194, 184, 255, 255),
    mainButtonLight = Color(197, 188, 255, 255),
    mainButtonDisconnected = Color(139, 139, 165, 255),
    innerShadow = Color(99, 94, 127, 255),
    backgroundDisconnected = Color(255, 255, 255, 255),
    pingCircles = Color(7, 138, 224, 255),
    shadowGlow = Color(0, 128, 160, 255),
    error = Color(156, 44, 30, 255),
)
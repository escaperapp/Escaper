package io.escaper.escaperapp.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import java.awt.Window
import java.awt.Color as AwtColor

@Composable
internal actual fun UpdateWindowBackground(background: Color) {
    LaunchedEffect(background) {
        val window = Window.getWindows().firstOrNull()
        if (window != null) {
            window.background = background.toAwtColor()
        }
    }
}

fun Color.toAwtColor(): AwtColor {
    val srgb = convert(ColorSpaces.Srgb)
    return AwtColor(
        srgb.red,
        srgb.green,
        srgb.blue,
        srgb.alpha
    )
}
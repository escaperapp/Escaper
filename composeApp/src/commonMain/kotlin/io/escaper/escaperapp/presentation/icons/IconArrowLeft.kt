package io.escaper.escaperapp.presentation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IconArrowLeft: ImageVector
    get() {
        if (ArrowLeft != null) {
            return ArrowLeft!!
        }
        ArrowLeft = ImageVector.Builder(
            name = "IconArrowLeft",
            defaultWidth = 800.dp,
            defaultHeight = 800.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 0f,
                strokeAlpha = 0f
            ) {
                moveTo(24f, 0f)
                lineToRelative(-0f, 24f)
                lineToRelative(-24f, 0f)
                lineToRelative(-0f, -24f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(13.83f, 19f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.78f, -0.37f)
                lineToRelative(-4.83f, -6f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -1.27f)
                lineToRelative(5f, -6f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.54f, 1.28f)
                lineTo(10.29f, 12f)
                lineToRelative(4.32f, 5.36f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.78f, 1.64f)
                close()
            }
        }.build()

        return ArrowLeft!!
    }

private var ArrowLeft: ImageVector? = null
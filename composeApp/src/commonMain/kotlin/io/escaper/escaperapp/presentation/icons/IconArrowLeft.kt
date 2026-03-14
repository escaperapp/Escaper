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
            name = "ArrowLeft",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(9.569f, 18.82f)
                curveTo(9.379f, 18.82f, 9.189f, 18.75f, 9.039f, 18.6f)
                lineTo(2.969f, 12.53f)
                curveTo(2.679f, 12.24f, 2.679f, 11.76f, 2.969f, 11.47f)
                lineTo(9.039f, 5.4f)
                curveTo(9.329f, 5.11f, 9.809f, 5.11f, 10.099f, 5.4f)
                curveTo(10.389f, 5.69f, 10.389f, 6.17f, 10.099f, 6.46f)
                lineTo(4.559f, 12f)
                lineTo(10.099f, 17.54f)
                curveTo(10.389f, 17.83f, 10.389f, 18.31f, 10.099f, 18.6f)
                curveTo(9.959f, 18.75f, 9.759f, 18.82f, 9.569f, 18.82f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(20.5f, 12.75f)
                horizontalLineTo(3.67f)
                curveTo(3.26f, 12.75f, 2.92f, 12.41f, 2.92f, 12f)
                curveTo(2.92f, 11.59f, 3.26f, 11.25f, 3.67f, 11.25f)
                horizontalLineTo(20.5f)
                curveTo(20.91f, 11.25f, 21.25f, 11.59f, 21.25f, 12f)
                curveTo(21.25f, 12.41f, 20.91f, 12.75f, 20.5f, 12.75f)
                close()
            }
        }.build()

        return ArrowLeft!!
    }

private var ArrowLeft: ImageVector? = null
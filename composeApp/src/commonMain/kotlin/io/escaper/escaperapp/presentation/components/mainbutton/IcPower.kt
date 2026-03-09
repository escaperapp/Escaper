package io.escaper.escaperapp.presentation.components.mainbutton

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IcPower: ImageVector
    get() {
        if (Power != null) {
            return Power!!
        }
        Power = ImageVector.Builder(
            name = "IcPower",
            defaultWidth = 94.dp,
            defaultHeight = 94.dp,
            viewportWidth = 94f,
            viewportHeight = 94f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 5.85106f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(29.375f, 29.916f)
                curveTo(25.065f, 33.532f, 21.973f, 38.388f, 20.517f, 43.822f)
                curveTo(19.061f, 49.257f, 19.313f, 55.008f, 21.237f, 60.294f)
                curveTo(23.162f, 65.581f, 26.666f, 70.148f, 31.275f, 73.375f)
                curveTo(35.884f, 76.602f, 41.374f, 78.333f, 47f, 78.333f)
                curveTo(52.626f, 78.333f, 58.116f, 76.602f, 62.725f, 73.375f)
                curveTo(67.334f, 70.148f, 70.838f, 65.581f, 72.763f, 60.294f)
                curveTo(74.687f, 55.008f, 74.939f, 49.257f, 73.483f, 43.822f)
                curveTo(72.027f, 38.388f, 68.935f, 33.532f, 64.625f, 29.916f)
                moveTo(47f, 15.667f)
                verticalLineTo(43.083f)
            }
        }.build()

        return Power!!
    }

private var Power: ImageVector? = null

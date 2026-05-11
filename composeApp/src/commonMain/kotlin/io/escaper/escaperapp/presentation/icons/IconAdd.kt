package io.escaper.escaperapp.presentation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IconAdd: ImageVector
    get() {
        if (Add != null) {
            return Add!!
        }
        Add = ImageVector.Builder(
            name = "IconAdd",
            defaultWidth = 800.dp,
            defaultHeight = 800.dp,
            viewportWidth = 512f,
            viewportHeight = 512f
        ).apply {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 32f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(256f, 112f)
                lineTo(256f, 400f)
            }
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 32f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(400f, 256f)
                lineTo(112f, 256f)
            }
        }.build()

        return Add!!
    }

private var Add: ImageVector? = null

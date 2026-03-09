package io.escaper.escaperapp.presentation.components.dropdown

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IcArrow: ImageVector
    get() {
        if (Arrow != null) {
            return Arrow!!
        }
        Arrow = ImageVector.Builder(
            name = "IcArrow",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5.667f, 3.333f)
                lineTo(10.333f, 8f)
                lineTo(5.667f, 12.667f)
            }
        }.build()

        return Arrow!!
    }

private var Arrow: ImageVector? = null

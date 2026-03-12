package io.escaper.escaperapp.presentation.components.mainbutton

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import io.escaper.escaperapp.presentation.common.EscaperTheme
import kotlin.math.pow

private const val PingAnimationDuration = 3000
private const val PingWavesCount = 3

private val BorderWidth = 10.dp

private val ButtonMinSize = 120.dp
private val ButtonMaxSize = 260.dp

@Composable
internal fun OnOffButton(
    isConnected: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val buttonColors = rememberButtonColors(isConnected)
    val pingProgress by animateInfinitePing(isConnected)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                if (pingProgress > 0f) {
                    drawDecorativePingWaves(
                        pingProgress = pingProgress,
                        buttonColors = buttonColors
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(
                    width = ButtonMaxSize,
                    height = ButtonMaxSize
                )
                .requiredSizeIn(
                    minWidth = ButtonMinSize,
                    minHeight = ButtonMinSize,
                )
                .aspectRatio(1f)
                .border(
                    width = BorderWidth,
                    color = buttonColors.borderColor,
                    shape = CircleShape
                )
                .mainButtonShadow(buttonColors)
                .clip(CircleShape)
                .clickable(
                    onClick = onClick
                )
                .mainButtonGradient(buttonColors),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = IcPower,
                contentDescription = null,
                tint = buttonColors.iconTint,
                modifier = Modifier.fillMaxSize(0.5f)
            )
        }

        if (isLoading) {
            CircularButtonLoader(
                loaderColor = EscaperTheme.colors.mainText
            )
        }
    }
}

@Composable
private fun rememberButtonColors(
    isConnected: Boolean,
): MainButtonColors {
    val colors = EscaperTheme.colors
    return remember(isConnected) {
        if (isConnected) {
            MainButtonColors(
                iconTint = colors.mainText,
                borderColor = colors.mainButtonLight,
                centerColor = colors.mainButtonLight,
                peripheryColor = colors.mainButtonDark,
                pingWaveColor = colors.pingCircles
            )
        } else {
            MainButtonColors(
                iconTint = colors.mainButtonDark,
                borderColor = colors.mainButtonDark,
                centerColor = colors.mainButtonDisconnected,
                peripheryColor = colors.innerShadow,
                pingWaveColor = colors.pingCircles
            )
        }
    }
}

@Immutable
private data class MainButtonColors(
    val iconTint: Color,
    val borderColor: Color,
    val centerColor: Color,
    val peripheryColor: Color,
    val pingWaveColor: Color,
)

private fun Modifier.mainButtonShadow(
    buttonColors: MainButtonColors,
): Modifier = this.dropShadow(
    shape = CircleShape,
    shadow = Shadow(
        radius = 64.dp,
        spread = 24.dp,
        color = buttonColors.borderColor,
        alpha = 0.2f
    )
)

private fun Modifier.mainButtonGradient(
    buttonColors: MainButtonColors,
): Modifier = this.background(
    shape = CircleShape,
    brush = Brush.radialGradient(
        colors = listOf(
            buttonColors.centerColor,
            buttonColors.centerColor,
            buttonColors.centerColor,
            buttonColors.peripheryColor,
        )
    )
)

private fun DrawScope.drawDecorativePingWaves(
    pingProgress: Float,
    buttonColors: MainButtonColors
) {
    val center = this.center
    val maxRadius = size.width * 1

    repeat(PingWavesCount) { index ->
        // Phase shift each wave
        val phaseOffset = index / PingWavesCount.toFloat()
        val progress =
            (pingProgress + phaseOffset) % 1f

        // Radius grows outward
        val radius = lerp(
            start = size.width / 2f,
            stop = maxRadius,
            fraction = progress
        )

        // Fade out as it expands
        val alpha = (1f - progress)
            .coerceIn(0f, 1f)
            .pow(2)

        drawCircle(
            color = buttonColors.pingWaveColor.copy(alpha = alpha),
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

@Composable
private fun animateInfinitePing(
    isConnected: Boolean,
): State<Float> {
    return if (isConnected) {
        val transition = rememberInfiniteTransition(
            label = "PingAnimationTransition"
        )
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = PingAnimationDuration,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
        )
    } else {
        mutableStateOf(0f)
    }
}

@Composable
private fun BoxScope.CircularButtonLoader(
    loaderColor: Color,
) {
    val transition = rememberInfiniteTransition(
        label = "CircularLoaderTransition"
    )
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
    )
    Box(
        Modifier
            .matchParentSize()
            .graphicsLayer {
                rotationZ = rotation
            }
            .drawWithCache {
                onDrawWithContent {
                    drawContent()

                    val drawingCenter = center
                    val borderWidth = BorderWidth.toPx()
                    val minDimension = size.minDimension - borderWidth
                    val halfDimension = minDimension / 2
                    val offsetX = drawingCenter.x - halfDimension
                    val offsetY = drawingCenter.y - halfDimension

                    drawArc(
                        brush = Brush.sweepGradient(
                            0f to Color.Transparent,
                            0.5f to loaderColor,
                            center = drawingCenter
                        ),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(offsetX, offsetY),
                        size = Size(minDimension, minDimension),
                        style = Stroke(
                            width = borderWidth,
                        )
                    )
                }
            }
    )
}
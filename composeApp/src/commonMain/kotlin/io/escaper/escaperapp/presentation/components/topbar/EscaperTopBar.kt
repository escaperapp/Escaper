package io.escaper.escaperapp.presentation.components.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.escaper.escaperapp.presentation.common.EscaperTheme
import io.escaper.escaperapp.presentation.icons.IconArrowLeft

private val DefaultPaddings = PaddingValues(8.dp)

private val TitlePaddings = 8.dp
private val DefaultMinHeight = 52.dp

@Immutable
data class ActionButton(
    val icon: ImageVector,
    val onClick: () -> Unit,
)

@Composable
fun EscaperTopBar(
    title: String,
    backButton: ImageVector = IconArrowLeft,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    vararg actions: ActionButton,
    backgroundColor: Color = Color.Transparent,
    contentPaddings: PaddingValues = DefaultPaddings,
    windowInsets: WindowInsets = WindowInsets.statusBars,
) = EscaperTopBar(
    title = {
        Text(
            text = title,
            style = EscaperTheme.typography.headlineLarge,
            fontSize = 24.sp,
            color = EscaperTheme.colors.mainText,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    },
    modifier = modifier,
    backButton = {
        TopBarButton(
            icon = backButton,
            onIconClick = onBackClick
        )
    },
    actions = if (actions.isNotEmpty()) {
        {
            Row {
                actions.forEach { action ->
                    TopBarButton(
                        icon = action.icon,
                        onIconClick = action.onClick
                    )
                }
            }
        }
    } else {
        null
    },
    backgroundColor = backgroundColor,
    contentPaddings = contentPaddings,
    windowInsets = windowInsets
)

@Composable
private fun TopBarButton(
    icon: ImageVector?,
    onIconClick: (() -> Unit)?
) {
    icon?.let { ic ->
        IconButton(
            shape = CircleShape,
            onClick = {
                onIconClick?.invoke()
            },
            enabled = onIconClick != null
        ) {
            Icon(
                imageVector = ic,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = EscaperTheme.colors.mainText
            )
        }
    }
}

@Composable
fun EscaperTopBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    backButton: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
    backgroundColor: Color = Color.Transparent,
    contentPaddings: PaddingValues = DefaultPaddings,
    windowInsets: WindowInsets = WindowInsets.statusBars,
) {
    Layout(
        modifier = modifier
            .background(backgroundColor)
            .defaultMinSize(minHeight = DefaultMinHeight)
            .fillMaxWidth()
            .padding(windowInsets.asPaddingValues())
            .padding(contentPaddings),
        measurePolicy = TopBarMeasurePolicy,
        content = {
            backButton?.let {
                Box(Modifier.layoutId(TopBarLayoutId.BACK_BUTTON)) {
                    it.invoke()
                }
            }
            Box(Modifier.layoutId(TopBarLayoutId.TITLE)) {
                title()
            }
            actions?.let {
                Box(Modifier.layoutId(TopBarLayoutId.ACTIONS)) {
                    it.invoke()
                }
            }
        }
    )
}

private enum class TopBarLayoutId {
    BACK_BUTTON,
    TITLE,
    ACTIONS
}

private object TopBarMeasurePolicy : MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints,
    ): MeasureResult {
        var backButton: Measurable? = null
        var title: Measurable? = null
        var actions: Measurable? = null

        for (measurable in measurables) {
            when (measurable.layoutId) {
                TopBarLayoutId.BACK_BUTTON -> {
                    backButton = measurable
                }

                TopBarLayoutId.TITLE -> {
                    title = measurable
                }

                TopBarLayoutId.ACTIONS -> {
                    actions = measurable
                }
            }
        }
        val relaxedConstraints = constraints.copy(minWidth = 0)
        val backButtonPlaceable = backButton?.measure(relaxedConstraints)
        val actionsPlaceable = actions?.measure(relaxedConstraints)
        val backWidth = backButtonPlaceable?.measuredWidth ?: 0
        val actionsWidth = actionsPlaceable?.measuredWidth ?: 0
        val titlePaddingsPx = TitlePaddings.roundToPx()

        val width = constraints.maxWidth
        val halfWidth = width / 2
        val titleSpaceOnLeft = (halfWidth - backWidth - titlePaddingsPx).coerceAtLeast(0)
        val titleSpaceOnRight = (halfWidth - actionsWidth - titlePaddingsPx).coerceAtLeast(0)
        val finalTitleSpace = minOf(titleSpaceOnLeft, titleSpaceOnRight)
        val titleMaxWidth = finalTitleSpace * 2

        val titlePlaceable = title?.measure(
            relaxedConstraints.copy(
                maxWidth = titleMaxWidth
            )
        )

        val height = maxOf(
            backButtonPlaceable?.height ?: 0,
            titlePlaceable?.height ?: 0,
            actionsPlaceable?.height ?: 0
        )

        return layout(width, height) {
            backButtonPlaceable?.placeRelative(0, 0)
            titlePlaceable?.placeRelative(
                (width - titlePlaceable.width) / 2,
                (height - titlePlaceable.height) / 2
            )
            actionsPlaceable?.placeRelative(
                width - actionsPlaceable.width,
                (height - actionsPlaceable.height) / 2
            )
        }
    }
}
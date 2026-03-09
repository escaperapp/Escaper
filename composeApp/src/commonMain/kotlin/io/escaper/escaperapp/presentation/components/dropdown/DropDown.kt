package io.escaper.escaperapp.presentation.components.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp
import io.escaper.escaperapp.presentation.common.EscaperTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun <T> EscaperDropdown(
    selectedItem: T?,
    items: List<T>,
    isConnected: Boolean,
    emptyPlaceholder: String,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSelectItem: (T) -> Unit,
    onFormatItem: (T) -> String,
    modifier: Modifier = Modifier,
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        DropdownSelectedLabel(
            text = selectedItem?.let(onFormatItem) ?: emptyPlaceholder,
            isConnected = isConnected,
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                onExpandedChange(false)
            },
            shape = LabelShape,
            containerColor = EscaperTheme.colorScheme.innerShadow
        ) {
            for (item in items) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelectItem(item)
                            onExpandedChange(false)
                        }
                        .padding(horizontal = 20.dp)
                        .defaultMinSize(minHeight = 48.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = onFormatItem(item),
                        color = EscaperTheme.colorScheme.mainText,
                        style = EscaperTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

private val LabelShape = RoundedCornerShape(24.dp)

@Composable
private fun DropdownSelectedLabel(
    text: String,
    isConnected: Boolean,
    modifier: Modifier = Modifier,
) {
    val background = if (isConnected) {
        EscaperTheme.colorScheme.mainButtonDark
    } else {
        EscaperTheme.colorScheme.backgroundDisconnected
    }
    val iconBackground = if (isConnected) {
        EscaperTheme.colorScheme.background
    } else {
        EscaperTheme.colorScheme.backgroundDisconnected
    }
    Row(
        modifier = modifier
            .dropShadow(
                shape = LabelShape,
                shadow = Shadow(
                    radius = 14.dp,
                    spread = 0.dp,
                    color = Color.Black.copy(alpha = 0.3f)
                )
            )
            .fillMaxWidth()
            .clip(LabelShape)
            .background(background)
            .defaultMinSize(minHeight = 80.dp)
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = EscaperTheme.colorScheme.mainText,
            style = EscaperTheme.typography.labelLarge
        )
        Icon(
            imageVector = IcArrow,
            contentDescription = null,
            tint = EscaperTheme.colorScheme.mainText,
            modifier = Modifier
                .rotate(90f)
                .dropShadow(
                    shape = CircleShape,
                    shadow = Shadow(
                        spread = 0.dp,
                        radius = 20.dp,
                        color = EscaperTheme.colorScheme.shadowGlow
                    )
                )
                .background(
                    color = iconBackground,
                    shape = CircleShape
                )
                .size(34.dp)
                .padding(9.dp)
        )
    }
}
package io.escaper.escaperapp.presentation.argsinput

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.escaper.escaperapp.presentation.common.EscaperTheme

@Composable
internal fun ToggleSwitcherInput(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onValueChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = EscaperTheme.typography.bodyLarge,
            color = EscaperTheme.colors.mainText
        )
        Switch(
            checked = isSelected,
            onCheckedChange = onValueChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = EscaperTheme.colors.mainButtonLight,
                checkedThumbColor = EscaperTheme.colors.innerShadow,
                uncheckedTrackColor = EscaperTheme.colors.mainButtonLight,
                uncheckedThumbColor = EscaperTheme.colors.innerShadow,
            )
        )
    }
}
package io.escaper.escaperapp.presentation.argsinput

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.escaper.escaperapp.presentation.common.EscaperTheme

@Composable
internal fun <T : Enum<T>> RadioButtonInput(
    options: List<T>,
    selectedOption: T?,
    onSelectOption: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        for (option in options) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        onSelectOption(option)
                    }.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = option.toString(),
                    modifier = Modifier.weight(1f),
                    style = EscaperTheme.typography.bodyMedium,
                    color = EscaperTheme.colors.mainText
                )
                RadioButton(
                    selected = selectedOption == option,
                    onClick = {
                        onSelectOption(option)
                    }
                )
            }
        }
    }
}
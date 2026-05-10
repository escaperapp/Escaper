package io.escaper.escaperapp.presentation.argsinput

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.escaper.escaperapp.presentation.common.EscaperTheme

@Composable
internal fun <T : Enum<T>> RadioButtonInput(
    options: List<T>,
    onSelectOption: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedOption: T? by rememberSaveable(
        saver = Saver(
            save = { it.value },
            restore = { savedOption ->
                mutableStateOf(options.find { it == savedOption })
            }
        )
    ) {
        mutableStateOf(null)
    }
    Column(modifier) {
        for (option in options) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
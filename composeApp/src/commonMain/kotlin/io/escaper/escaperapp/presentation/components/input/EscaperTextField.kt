package io.escaper.escaperapp.presentation.components.input

import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.escaper.escaperapp.presentation.common.EscaperTheme

@Composable
internal fun EscaperTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    topLabel: String? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = topLabel?.let { label ->
            {
                Text(
                    text = label,
                    style = EscaperTheme.typography.labelMedium
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = EscaperTheme.colors.mainText,
            unfocusedTextColor = EscaperTheme.colors.mainText,
            disabledTextColor = EscaperTheme.colors.mainText.copy(alpha = 0.5f),
            errorTextColor = EscaperTheme.colors.error,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            cursorColor = EscaperTheme.colors.mainText,
            errorCursorColor = EscaperTheme.colors.mainText,
            selectionColors = LocalTextSelectionColors.current,
            focusedLabelColor = EscaperTheme.colors.mainText,
            unfocusedLabelColor = EscaperTheme.colors.mainText,
            disabledLabelColor = EscaperTheme.colors.mainText.copy(alpha = 0.5f),
            errorLabelColor = EscaperTheme.colors.error,
            focusedPlaceholderColor = EscaperTheme.colors.mainText,
            unfocusedPlaceholderColor = EscaperTheme.colors.mainText,
            disabledPlaceholderColor = EscaperTheme.colors.mainText,
            errorPlaceholderColor = EscaperTheme.colors.error,
            focusedSupportingTextColor = EscaperTheme.colors.mainText,
            unfocusedSupportingTextColor = EscaperTheme.colors.mainText,
            disabledSupportingTextColor = EscaperTheme.colors.mainText,
            errorSupportingTextColor = EscaperTheme.colors.error,
            focusedBorderColor = EscaperTheme.colors.mainText,
            unfocusedBorderColor = EscaperTheme.colors.mainButtonLight,
            disabledBorderColor = EscaperTheme.colors.mainButtonDark,
            errorBorderColor = EscaperTheme.colors.error,
        )
    )
}
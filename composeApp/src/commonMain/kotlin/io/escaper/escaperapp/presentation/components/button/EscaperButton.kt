package io.escaper.escaperapp.presentation.components.button

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import io.escaper.escaperapp.presentation.common.EscaperTheme

@Composable
internal fun EscaperButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = EscaperTheme.colors.mainButtonLight,
            contentColor = EscaperTheme.colors.mainText,
            disabledContainerColor = EscaperTheme.colors.mainButtonLight.copy(alpha = 0.5f),
            disabledContentColor = EscaperTheme.colors.mainText.copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = title,
            style = EscaperTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
internal fun EscaperButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = EscaperTheme.colors.mainButtonLight,
            contentColor = EscaperTheme.colors.mainText,
            disabledContainerColor = EscaperTheme.colors.mainButtonLight.copy(alpha = 0.5f),
            disabledContentColor = EscaperTheme.colors.mainText.copy(alpha = 0.5f)
        ),
        content = content,
    )
}
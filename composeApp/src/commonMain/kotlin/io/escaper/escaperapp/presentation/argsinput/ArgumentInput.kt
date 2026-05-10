package io.escaper.escaperapp.presentation.argsinput

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.argument_input_save_label
import io.escaper.escaperapp.domain.args.ArgumentKey
import io.escaper.escaperapp.domain.args.TpwsDebugMode
import io.escaper.escaperapp.domain.args.ZapretArgument
import io.escaper.escaperapp.domain.args.tpws.TpwsDebugArgument
import io.escaper.escaperapp.presentation.common.EscaperTheme
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ArgumentInput(
    argumentType: ArgumentKey,
    onSelectArgument: (ZapretArgument<*, *>) -> Unit,
) {
    when (argumentType) {
        ArgumentKey.TpwsDebugModeArg -> RadioButtonInput(
            options = TpwsDebugMode.entries,
            onSelectOption = { mode ->
                onSelectArgument(TpwsDebugArgument(mode))
            }
        )

        ArgumentKey.DryRunArg,
        ArgumentKey.VersionArg,
        ArgumentKey.DaemonArg,
        ArgumentKey.PidFileArg,
        ArgumentKey.UserArg,
        ArgumentKey.UidArg,
        ArgumentKey.BindAddrArg,
        ArgumentKey.BindLinkLocalArg,
        ArgumentKey.BindIface4Arg,
        ArgumentKey.BindIface6Arg,
            -> Unit
    }
}

@Composable
private fun <T : Enum<T>> RadioButtonInput(
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
            Row(Modifier.fillMaxWidth()) {
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
        Button(
            onClick = {
                selectedOption?.let {
                    onSelectOption(it)
                }
            },
            enabled = selectedOption != null,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = EscaperTheme.colors.mainButtonLight,
                contentColor = EscaperTheme.colors.mainText,
            )
        ) {
            Text(
                text = stringResource(EscaperRes.string.argument_input_save_label),
                style = EscaperTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
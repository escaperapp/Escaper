package io.escaper.escaperapp.presentation.argsinput

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.argument_input_save_label
import io.escaper.escaperapp.domain.ExecutableType
import io.escaper.escaperapp.domain.args.AnyZapretArgument
import io.escaper.escaperapp.domain.args.ArgumentKey
import io.escaper.escaperapp.domain.args.TpwsDebugMode
import io.escaper.escaperapp.domain.args.ZapretArgument
import io.escaper.escaperapp.domain.args.tpws.TpwsDebugArgument
import io.escaper.escaperapp.presentation.common.EscaperTheme
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ArgumentInput(
    argumentType: ArgumentKey,
    executableType: ExecutableType,
    onSelectArgument: (AnyZapretArgument) -> Unit,
) {
    var selectedArgument: AnyZapretArgument? by rememberSaveable(
        saver = ZapretArgument.Saver(executableType)
    ) {
        mutableStateOf(null)
    }
    Column {
        when (argumentType) {
            ArgumentKey.TpwsDebugModeArg -> {
                RadioButtonInput(
                    options = TpwsDebugMode.entries,
                    onSelectOption = { mode ->
                        onSelectArgument(TpwsDebugArgument(mode))
                    }
                )
            }

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
        Button(
            onClick = {
                selectedArgument?.let {
                    onSelectArgument(it)
                }
            },
            enabled = selectedArgument != null,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
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

private fun ZapretArgument.Companion.Saver(
    executableType: ExecutableType,
): Saver<MutableState<AnyZapretArgument?>, String> = Saver(
    save = { it.value?.asStringArg().orEmpty() },
    restore = { rawValue ->
        mutableStateOf(
            ZapretArgument.fromStringArg(
                arg = rawValue,
                executableType = executableType
            )
        )
    }
)

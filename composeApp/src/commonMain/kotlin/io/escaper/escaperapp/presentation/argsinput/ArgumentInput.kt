package io.escaper.escaperapp.presentation.argsinput

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.argument_input_save_label
import io.escaper.escaperapp.domain.ExecutableType
import io.escaper.escaperapp.domain.args.AnyZapretArgument
import io.escaper.escaperapp.domain.args.ArgumentKey
import io.escaper.escaperapp.domain.args.BindAddressArgument
import io.escaper.escaperapp.domain.args.BindInterface4Argument
import io.escaper.escaperapp.domain.args.BindInterface6Argument
import io.escaper.escaperapp.domain.args.BindLinkLocalArgument
import io.escaper.escaperapp.domain.args.DaemonArgument
import io.escaper.escaperapp.domain.args.DoubleArgument
import io.escaper.escaperapp.domain.args.DryRunArgument
import io.escaper.escaperapp.domain.args.FlagArgument
import io.escaper.escaperapp.domain.args.IntArgument
import io.escaper.escaperapp.domain.args.ListArgument
import io.escaper.escaperapp.domain.args.NewArgument
import io.escaper.escaperapp.domain.args.PairArgument
import io.escaper.escaperapp.domain.args.PidFileArgument
import io.escaper.escaperapp.domain.args.StringArgument
import io.escaper.escaperapp.domain.args.TpwsDebugMode
import io.escaper.escaperapp.domain.args.TpwsDebugArgument
import io.escaper.escaperapp.domain.args.UidArgument
import io.escaper.escaperapp.domain.args.UserArgument
import io.escaper.escaperapp.domain.args.VersionArgument
import io.escaper.escaperapp.presentation.components.button.EscaperButton
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ArgumentInput(
    argumentState: NullableArgumentState,
    onConfirmArgument: (AnyZapretArgument) -> Unit,
) {
    Column {
        when (argumentState.selectedValue ?: argumentState.preInitValue) {
            is DaemonArgument -> TODO()
            is DryRunArgument -> TODO()
            is VersionArgument -> TODO()
            is TpwsDebugArgument -> {
                RadioButtonInput(
                    options = TpwsDebugMode.entries,
                    selectedOption = argumentState.typedSelectedValue(),
                    onSelectOption = { mode ->
                        argumentState.selectedValue = TpwsDebugArgument(mode)
                    }
                )
            }
            is UidArgument -> TODO()
            is BindAddressArgument -> TODO()
            is BindInterface4Argument -> TODO()
            is BindInterface6Argument -> TODO()
            is BindLinkLocalArgument -> TODO()
            is PidFileArgument -> TODO()
            is UserArgument -> TODO()
            is NewArgument -> Unit
            null -> Unit
        }
        EscaperButton(
            title = stringResource(EscaperRes.string.argument_input_save_label),
            onClick = {
                argumentState.selectedValue?.let(onConfirmArgument)
            },
            enabled = argumentState.selectedValue != null,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )
    }
}
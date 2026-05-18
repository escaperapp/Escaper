package io.escaper.escaperapp.presentation.argsinput

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.argument_input_save_label
import io.escaper.escaperapp.domain.args.AnyZapretArgument
import io.escaper.escaperapp.domain.args.BindAddressArgument
import io.escaper.escaperapp.domain.args.BindInterface4Argument
import io.escaper.escaperapp.domain.args.BindInterface6Argument
import io.escaper.escaperapp.domain.args.BindLinkLocalArgument
import io.escaper.escaperapp.domain.args.DaemonArgument
import io.escaper.escaperapp.domain.args.DryRunArgument
import io.escaper.escaperapp.domain.args.FlagArgument
import io.escaper.escaperapp.domain.args.NewArgument
import io.escaper.escaperapp.domain.args.PidFileArgument
import io.escaper.escaperapp.domain.args.TpwsDebugArgument
import io.escaper.escaperapp.domain.args.TpwsDebugMode
import io.escaper.escaperapp.domain.args.UidArgument
import io.escaper.escaperapp.domain.args.UserArgument
import io.escaper.escaperapp.domain.args.VersionArgument
import io.escaper.escaperapp.presentation.components.button.EscaperButton
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ArgumentInput(
    argumentState: NullableArgumentState,
    onConfirmArgument: (AnyZapretArgument?) -> Unit,
) {
    val baseValue = argumentState.anyValue
    val allowNullValue = baseValue is FlagArgument
    Column {
        when (baseValue) {
            DaemonArgument,
            DryRunArgument,
            VersionArgument -> {
                val description = baseValue.name.toDescription() ?: return
                ToggleSwitcherInput(
                    title = stringResource(description),
                    isSelected = argumentState.selectedValue != null,
                    onValueChange = { isSelected ->
                        argumentState.setSelected(
                            if (isSelected) {
                                argumentState.preInitValue
                            } else {
                                null
                            }
                        )
                    }
                )
            }

            is TpwsDebugArgument -> {
                RadioButtonInput(
                    options = TpwsDebugMode.entries,
                    selectedOption = argumentState.typedSelectedValue(),
                    onSelectOption = { mode ->
                        argumentState.setSelected(TpwsDebugArgument(mode))
                    }
                )
            }

            is UidArgument -> Unit
            is BindAddressArgument -> Unit
            is BindInterface4Argument -> Unit
            is BindInterface6Argument -> Unit
            is BindLinkLocalArgument -> Unit
            is PidFileArgument -> Unit
            is UserArgument -> Unit

            is NewArgument -> Unit
            null -> Unit
        }
        EscaperButton(
            title = stringResource(EscaperRes.string.argument_input_save_label),
            onClick = {
                onConfirmArgument(argumentState.selectedValue)
            },
            enabled = argumentState.selectedValue != null || allowNullValue,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )
    }
}
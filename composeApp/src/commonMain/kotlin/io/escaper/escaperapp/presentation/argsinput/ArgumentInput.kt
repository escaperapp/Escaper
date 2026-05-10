package io.escaper.escaperapp.presentation.argsinput

import androidx.compose.runtime.Composable
import io.escaper.escaperapp.domain.args.AnyZapretArgument
import io.escaper.escaperapp.domain.args.ArgumentKey
import io.escaper.escaperapp.domain.args.TpwsDebugMode
import io.escaper.escaperapp.domain.args.tpws.TpwsDebugArgument

@Composable
internal fun ArgumentInput(
    argumentType: ArgumentKey,
    onSelectArgument: (AnyZapretArgument) -> Unit,
) {
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
}

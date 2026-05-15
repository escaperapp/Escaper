package io.escaper.escaperapp.presentation.argsinput

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import io.escaper.escaperapp.domain.ExecutableType
import io.escaper.escaperapp.domain.args.AnyZapretArgument
import io.escaper.escaperapp.domain.args.ArgumentKey
import io.escaper.escaperapp.domain.args.BindAddressArgument
import io.escaper.escaperapp.domain.args.BindInterface4Argument
import io.escaper.escaperapp.domain.args.BindLinkLocalArgument
import io.escaper.escaperapp.domain.args.BindLinkLocalMode
import io.escaper.escaperapp.domain.args.DaemonArgument
import io.escaper.escaperapp.domain.args.DryRunArgument
import io.escaper.escaperapp.domain.args.NewArgument
import io.escaper.escaperapp.domain.args.PidFileArgument
import io.escaper.escaperapp.domain.args.StringValue
import io.escaper.escaperapp.domain.args.TpwsDebugArgument
import io.escaper.escaperapp.domain.args.TpwsDebugMode
import io.escaper.escaperapp.domain.args.UserArgument
import io.escaper.escaperapp.domain.args.VersionArgument
import io.escaper.escaperapp.domain.args.ZapretArgument

@Stable
class NullableArgumentState {
    var preInitValue: AnyZapretArgument? by mutableStateOf(null)
    var selectedValue: AnyZapretArgument? by mutableStateOf(null)

    inline fun <reified T> typedSelectedValue(): T? = selectedValue as? T

    fun preInitByKey(
        key: ArgumentKey,
    ) {
        preInitValue = when (key) {
            ArgumentKey.TpwsDebugModeArg -> {
                 TpwsDebugArgument(TpwsDebugMode.QUIET)
            }

            ArgumentKey.DryRunArg -> DryRunArgument
            ArgumentKey.VersionArg -> VersionArgument
            ArgumentKey.DaemonArg -> DaemonArgument
            ArgumentKey.PidFileArg -> {
                PidFileArgument(StringValue.Empty)
            }

            ArgumentKey.UserArg -> {
                UserArgument(StringValue.Empty)
            }

            ArgumentKey.UidArg -> {
                UserArgument(StringValue.Empty)
            }

            ArgumentKey.BindAddrArg -> {
                BindAddressArgument(StringValue.Empty)
            }

            ArgumentKey.BindLinkLocalArg -> {
                BindLinkLocalArgument(BindLinkLocalMode.NO)
            }

            ArgumentKey.BindIface4Arg -> {
                BindInterface4Argument(StringValue.Empty)
            }

            ArgumentKey.BindIface6Arg -> {
                BindInterface4Argument(StringValue.Empty)
            }

            ArgumentKey.NewArg -> NewArgument
        }
    }

    fun initByPreselected(
        argument: AnyZapretArgument?,
    ) {
        preInitValue = argument
    }

    companion object {
        fun Saver(
            executableType: ExecutableType,
        ): Saver<NullableArgumentState, Array<String>> = Saver(
            save = { arrayOf(
                it.preInitValue?.asStringArg().orEmpty(),
                it.selectedValue?.asStringArg().orEmpty()
            ) },
            restore = { rawValue ->
                NullableArgumentState().apply {
                    preInitValue = ZapretArgument.fromStringArg(
                        arg = rawValue.getOrNull(0),
                        executableType = executableType
                    )
                    selectedValue = ZapretArgument.fromStringArg(
                        arg = rawValue.getOrNull(1),
                        executableType = executableType
                    )
                }
            }
        )
    }
}
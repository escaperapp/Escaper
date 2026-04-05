package io.escaper.escaperapp.domain.args.tpws

import io.escaper.escaperapp.domain.args.ArgumentKey
import io.escaper.escaperapp.domain.args.IntArgument
import io.escaper.escaperapp.domain.args.TpwsDebugMode

internal data class TpwsDebugArgument(
    override val value: TpwsDebugMode,
) : IntArgument<TpwsDebugMode>(
    name = ArgumentKey.TpwsDebugModeArg,
    value = value,
) {
    override val isTpwsCompatible: Boolean = true
}

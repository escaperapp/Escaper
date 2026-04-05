package io.escaper.escaperapp.domain.args.tpws

import io.escaper.escaperapp.domain.args.IntArgument
import io.escaper.escaperapp.domain.args.TpwsDebugMode

internal data class TpwsDebugArgument(
    override val name: String,
    override val value: TpwsDebugMode,
) : IntArgument<TpwsDebugMode>(
    name = name,
    value = value,
) {
    override val isTpwsCompatible: Boolean = true
}

package io.escaper.escaperapp.domain.args

internal data class TpwsDebugArgument(
    override val value: TpwsDebugMode,
) : IntArgument<TpwsDebugMode>(
    name = ArgumentKey.TpwsDebugModeArg,
    value = value,
)
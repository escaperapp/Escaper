package io.escaper.escaperapp.domain.args

data class BindLinkLocalArgument(
    override val value: BindLinkLocalMode,
) : StringArgument<BindLinkLocalMode>(
    name = ArgumentKey.BindLinkLocalArg,
    value = value,
)
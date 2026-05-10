package io.escaper.escaperapp.domain.args.tpws

import io.escaper.escaperapp.domain.args.ArgumentKey
import io.escaper.escaperapp.domain.args.BindLinkLocalMode
import io.escaper.escaperapp.domain.args.StringArgument

data class BindLinkLocalArgument(
    override val value: BindLinkLocalMode,
) : StringArgument<BindLinkLocalMode>(
    name = ArgumentKey.BindLinkLocalArg,
    value = value,
)
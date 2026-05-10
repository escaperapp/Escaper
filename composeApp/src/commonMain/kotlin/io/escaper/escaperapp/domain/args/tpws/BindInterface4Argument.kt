package io.escaper.escaperapp.domain.args.tpws

import io.escaper.escaperapp.domain.args.ArgumentKey
import io.escaper.escaperapp.domain.args.StringArgument
import io.escaper.escaperapp.domain.args.StringValue

data class BindInterface4Argument(
    override val value: StringValue,
) : StringArgument<StringValue>(
    name = ArgumentKey.BindIface4Arg,
    value = value,
)
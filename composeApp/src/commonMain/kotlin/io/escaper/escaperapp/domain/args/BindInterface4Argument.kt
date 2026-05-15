package io.escaper.escaperapp.domain.args

data class BindInterface4Argument(
    override val value: StringValue,
) : StringArgument<StringValue>(
    name = ArgumentKey.BindIface4Arg,
    value = value,
)
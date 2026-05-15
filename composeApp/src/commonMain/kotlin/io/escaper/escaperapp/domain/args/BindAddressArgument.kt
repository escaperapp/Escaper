package io.escaper.escaperapp.domain.args

data class BindAddressArgument(
    override val value: StringValue,
) : StringArgument<StringValue>(
    name = ArgumentKey.BindAddrArg,
    value = value,
)
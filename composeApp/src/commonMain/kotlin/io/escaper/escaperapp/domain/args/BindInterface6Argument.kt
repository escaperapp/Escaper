package io.escaper.escaperapp.domain.args

data class BindInterface6Argument(
    override val value: StringValue,
) : StringArgument<StringValue>(
    name = ArgumentKey.BindIface6Arg,
    value = value,
)
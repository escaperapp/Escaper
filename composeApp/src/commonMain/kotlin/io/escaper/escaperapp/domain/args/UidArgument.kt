package io.escaper.escaperapp.domain.args

data class UidArgument(
    override val value: UidArgumentValue,
) : PairArgument<Int, Int?, UidArgumentValue>(
    name = ArgumentKey.UidArg,
    value = value,
)
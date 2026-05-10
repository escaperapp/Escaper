package io.escaper.escaperapp.domain.args.tpws

import io.escaper.escaperapp.domain.args.ArgumentKey
import io.escaper.escaperapp.domain.args.PairArgument
import io.escaper.escaperapp.domain.args.UidArgumentValue

data class UidArgument(
    override val value: UidArgumentValue,
) : PairArgument<Int, Int?, UidArgumentValue>(
    name = ArgumentKey.UidArg,
    value = value,
)
package io.escaper.escaperapp.domain.args

sealed class  PairArgument<A, B, V: PairValue<A, B>>(
    override val name: ArgumentKey,
    override val value: V,
) : ZapretArgument<Pair<A, B>, V>
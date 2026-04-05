package io.escaper.escaperapp.domain.args

abstract class IntArgument<V : IntValue>(
    override val name: ArgumentKey,
    override val value: V,
) : ZapretArgument<Int, V>
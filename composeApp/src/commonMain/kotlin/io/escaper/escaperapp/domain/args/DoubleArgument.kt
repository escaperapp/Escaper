package io.escaper.escaperapp.domain.args

abstract class DoubleArgument<V : DoubleValue>(
    override val name: ArgumentKey,
    override val value: V,
) : ZapretArgument<Double, V>
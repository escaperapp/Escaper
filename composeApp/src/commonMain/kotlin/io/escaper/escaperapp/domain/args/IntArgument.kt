package io.escaper.escaperapp.domain.args

abstract class IntArgument<V : IntValue>(
    override val name: String,
    override val value: V,
) : ZapretArgument<Int, V> {
    override fun asStringArg(): String = "--$name=${value.toCli()}"
}
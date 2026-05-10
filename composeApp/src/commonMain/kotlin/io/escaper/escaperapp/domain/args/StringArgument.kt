package io.escaper.escaperapp.domain.args

abstract class StringArgument<V : StringValue>(
    override val name: ArgumentKey,
    override val value: V
) : ZapretArgument<String, V>
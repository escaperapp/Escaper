package io.escaper.escaperapp.domain.args

sealed class StringArgument<V : StringValue>(
    override val name: ArgumentKey,
    override val value: V
) : ZapretArgument<String, V>
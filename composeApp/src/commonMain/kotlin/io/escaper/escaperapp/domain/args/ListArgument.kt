package io.escaper.escaperapp.domain.args

sealed class ListArgument<V : ListValue>(
    override val name: ArgumentKey,
    override val value: V,
) : ZapretArgument<List<String>, ArgValue<List<String>>>
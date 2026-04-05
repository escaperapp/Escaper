package io.escaper.escaperapp.domain.args

abstract class ListArgument<V : ListValue>(
    override val name: ArgumentKey,
    override val value: V,
) : ZapretArgument<List<String>, ArgValue<List<String>>>
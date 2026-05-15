package io.escaper.escaperapp.domain.args

sealed class FlagArgument(
    override val name: ArgumentKey,
) : ZapretArgument<Any, ArgValue<Any>> {
    override val value: ArgValue<Any> = error("Flag arguments have no associated value")

    override fun asStringArg(): String = "$ArgPrefix${name.cliKey}"
}
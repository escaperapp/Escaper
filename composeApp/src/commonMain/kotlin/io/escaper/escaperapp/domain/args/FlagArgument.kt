package io.escaper.escaperapp.domain.args

private object FlagArgValue : ArgValue<Any> {
    override val rawValue: Any = object : Any() {}
    override fun toCli(): String = ""
}

sealed class FlagArgument(
    override val name: ArgumentKey,
) : ZapretArgument<Any, ArgValue<Any>> {
    override val value: ArgValue<Any> = FlagArgValue

    override fun asStringArg(): String = "$ArgPrefix${name.cliKey}"
}
package io.escaper.escaperapp.domain.args

abstract class FlagArgument(
    override val name: ArgumentKey,
) : ZapretArgument<Nothing, ArgValue<Nothing>> {
    override val value: ArgValue<Nothing> = error("Flag arguments have no associated value")

    override fun asStringArg(): String = "$ArgPrefix$name"
}
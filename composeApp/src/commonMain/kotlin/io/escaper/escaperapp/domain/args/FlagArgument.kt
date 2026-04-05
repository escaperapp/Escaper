package io.escaper.escaperapp.domain.args

data class FlagArgument(
    override val name: String,
) : ZapretArgument {
    override fun asStringArg(): String = "--$name"
}
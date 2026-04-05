package io.escaper.escaperapp.domain.args

data class StringArgument(
    override val name: String,
    val value: String
) : ZapretArgument {
    override fun asStringArg(): String = "--$name=$value"
}
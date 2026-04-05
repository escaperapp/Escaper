package io.escaper.escaperapp.domain.args

data class DoubleArgument(
    override val name: String,
    val value: Double
) : ZapretArgument {
    override fun asStringArg(): String = "--$name=$value"
}
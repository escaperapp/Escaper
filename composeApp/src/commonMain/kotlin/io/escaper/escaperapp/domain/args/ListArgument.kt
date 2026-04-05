package io.escaper.escaperapp.domain.args

data class ListArgument(
    override val name: String,
    val values: List<String>
) : ZapretArgument {
    override fun asStringArg(): String =
        "--$name=${values.joinToString(",")}"
}
package io.escaper.escaperapp.domain.args

import kotlin.jvm.JvmInline

sealed interface RawValueInput {
    data object FlagInput : RawValueInput
    @JvmInline
    value class ListInput(
        val values: List<String>,
    ) : RawValueInput

    @JvmInline
    value class DoubleInput(
        val value: Double,
    ) : RawValueInput

    @JvmInline
    value class IntInput(
        val value: Int,
    ) : RawValueInput

    @JvmInline
    value class StringInput(
        val value: String,
    ) : RawValueInput

    fun asFlag(): FlagInput? = this as? FlagInput

    fun asList(): ListInput? = this as? ListInput

    fun asDouble(): DoubleInput? = this as? DoubleInput

    fun asInt(): IntInput? = this as? IntInput

    fun asString(): StringInput? = this as? StringInput
}


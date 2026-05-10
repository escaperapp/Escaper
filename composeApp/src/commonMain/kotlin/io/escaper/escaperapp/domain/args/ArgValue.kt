package io.escaper.escaperapp.domain.args

sealed interface ArgValue<T : Any> {
    val rawValue: T

    fun toCli(): String
}

interface ArgValueParser {
    fun fromCli(cliValue: RawValueInput): ArgValue<*>?
}

sealed interface IntValue : ArgValue<Int> {
    override fun toCli(): String = rawValue.toString()
}

sealed interface DoubleValue : ArgValue<Double> {
    override fun toCli(): String = rawValue.toString()
}

sealed interface StringValue : ArgValue<String> {
    override fun toCli(): String = rawValue
}

private data class StringValueImpl(
    override val rawValue: String,
) : StringValue

fun StringValue(rawValue: String): StringValue = StringValueImpl(rawValue)

sealed interface ListValue : ArgValue<List<String>> {
    override fun toCli(): String = rawValue.joinToString(ValuesListDelimiter)
}

sealed interface PairValue<A, B> : ArgValue<Pair<A, B>> {
    override fun toCli(): String {
        return when {
            rawValue.first == null && rawValue.second == null -> ""
            rawValue.first != null && rawValue.second == null -> rawValue.first.toString()
            rawValue.first == null && rawValue.second != null -> rawValue.second.toString()
            else -> "${rawValue.first}:${rawValue.second}"
        }
    }
}

private data class PairValueImpl<A, B>(
    override val rawValue: Pair<A, B>,
) : PairValue<A, B>

fun <A, B> PairValue(rawValue: Pair<A, B>): PairValue<A, B> = PairValueImpl(rawValue)

internal enum class NfwsDebugMode(
    override val rawValue: Int,
) : IntValue {
    RELEASE(0),
    DEBUG(1);

    override fun toCli(): String {
        return rawValue.toString()
    }

    companion object : ArgValueParser {
        override fun fromCli(cliValue: RawValueInput): IntValue? {
            val intValue = cliValue.asInt()
            return when (intValue?.value) {
                0 -> RELEASE
                1 -> DEBUG
                else -> null
            }
        }
    }
}

internal enum class TpwsDebugMode(
    override val rawValue: Int,
) : IntValue {
    QUIET(0),
    VERBOSE(1),
    DEBUGGING(2);

    override fun toCli(): String {
        return rawValue.toString()
    }

    companion object : ArgValueParser {
        override fun fromCli(cliValue: RawValueInput): TpwsDebugMode? {
            val intValueInput = cliValue.asInt()
            return when (intValueInput?.value) {
                0 -> QUIET
                1 -> VERBOSE
                2 -> DEBUGGING
                else -> null
            }
        }
    }
}
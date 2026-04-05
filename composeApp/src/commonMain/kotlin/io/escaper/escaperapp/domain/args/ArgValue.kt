package io.escaper.escaperapp.domain.args

sealed interface ArgValue<T : Any> {
    val rawValue: T

    fun fromCli(cliValue: String): ArgValue<T>

    fun toCli(): String
}

sealed interface IntValue : ArgValue<Int>

internal enum class NfwsDebugMode(
    override val rawValue: Int,
) : IntValue {
    RELEASE(0),
    DEBUG(1);

    override fun toCli(): String {
        return rawValue.toString()
    }

    override fun fromCli(cliValue: String): IntValue {
        val intValue = cliValue.toIntOrNull()
        return when (intValue) {
            0 -> RELEASE
            1 -> DEBUG
            else -> error("Invalid cli value")
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

    override fun fromCli(cliValue: String): TpwsDebugMode {
        val intValue = cliValue.toIntOrNull()
        return when (intValue) {
            0 -> QUIET
            1 -> VERBOSE
            2 -> DEBUGGING
            else -> error("Invalid cli value")
        }
    }
}
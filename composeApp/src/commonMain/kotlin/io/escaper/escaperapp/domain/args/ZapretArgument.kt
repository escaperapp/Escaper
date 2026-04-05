package io.escaper.escaperapp.domain.args

import io.escaper.escaperapp.domain.ExecutableType

sealed interface ZapretArgument<R : Any, V : ArgValue<R>> {

    val name: String

    val value: V

    fun asStringArg(): String

    val isWinwsCompatible: Boolean get() = false

    val isNfqsCompatible: Boolean get() = false

    val isTpwsCompatible: Boolean get() = false

    fun isCompatibleWithExecutable(
        type: ExecutableType,
    ): Boolean {
        return when (type) {
            ExecutableType.Winws -> isWinwsCompatible
            ExecutableType.Tpws -> isTpwsCompatible
            ExecutableType.Nfqs -> isNfqsCompatible
        }
    }

    companion object {
        fun fromStringArg(arg: String): ZapretArgument<*, *>? {
            if (!arg.startsWith("--")) {
                return null
            }

            val body = arg.removePrefix("--")

            // flag (no =)
            if (!body.contains("=")) {
                return FlagArgument(body)
            }

            val (key, rawValue) = body.split("=", limit = 2)

            // detect list (comma-separated)
            return when {
                rawValue.contains(",") -> ListArgument(key, rawValue.split(","))
                rawValue.matches(Regex("""-?\d+""")) -> IntArgument(key, rawValue.toInt())
                rawValue.matches(Regex("""-?\d+\.\d+""")) -> DoubleArgument(
                    key,
                    rawValue.toDouble()
                )

                else -> StringArgument(key, rawValue)
            }
        }
    }
}
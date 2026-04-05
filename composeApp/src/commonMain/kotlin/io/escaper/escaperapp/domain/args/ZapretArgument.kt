package io.escaper.escaperapp.domain.args

import io.escaper.escaperapp.domain.ExecutableType

sealed interface ZapretArgument<R : Any, V : ArgValue<R>> {

    val name: ArgumentKey

    val value: V

    fun asStringArg(): String = "$ArgPrefix$name$KeyValueDelimiter${value.toCli()}"

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
        private val IntRegex = Regex("""-?\d+""")
        private val DoubleRegex = Regex("""-?\d+\.\d+""")

        fun fromStringArg(
            arg: String,
            executableType: ExecutableType,
        ): ZapretArgument<*, *>? {
            if (!arg.startsWith(ArgPrefix)) {
                return null
            }

            val body = arg.removePrefix(ArgPrefix)

            // flag (no =)
            if (!body.contains(KeyValueDelimiter)) {
                return ArgumentKey.parse(
                    cliKey = body,
                    value = RawValueInput.FlagInput,
                    executableType = executableType
                )
            }

            val (key, rawValue) = body.split(
                KeyValueDelimiter,
                limit = 2
            )

            // detect list (comma-separated)
            return when {
                rawValue.contains(ValuesListDelimiter) -> {
                    ArgumentKey.parse(
                        cliKey = key,
                        executableType = executableType,
                        value = RawValueInput.ListInput(
                            rawValue.split(ValuesListDelimiter)
                        )
                    )
                }

                rawValue.matches(IntRegex) -> {
                    ArgumentKey.parse(
                        cliKey = key,
                        executableType = executableType,
                        value = RawValueInput.IntInput(rawValue.toInt())
                    )
                }

                rawValue.matches(DoubleRegex) -> {
                    ArgumentKey.parse(
                        cliKey = key,
                        executableType = executableType,
                        value = RawValueInput.DoubleInput(rawValue.toDouble())
                    )
                }

                else -> {
                    ArgumentKey.parse(
                        cliKey = key,
                        executableType = executableType,
                        value = RawValueInput.StringInput(rawValue)
                    )
                }
            }
        }
    }
}
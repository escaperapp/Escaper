package io.escaper.escaperapp.domain.args

import io.escaper.escaperapp.domain.ExecutableType
import io.escaper.escaperapp.domain.args.tpws.TpwsDebugArgument

enum class ArgumentKey(
    val cliKey: String,
    val executableType: ExecutableType,
    val parser: (RawValueInput) -> ZapretArgument<*, *>?
) {
    TpwsDebugModeArg(
        cliKey = "debug",
        executableType = ExecutableType.Tpws,
        parser = { value -> TpwsDebugMode.fromCli(value)?.let(::TpwsDebugArgument) }
    ),
    ;

    companion object {
        private fun getMapForExecType(type: ExecutableType) = entries
            .filter { it.executableType == type }
            .associateBy { it.cliKey }

        private val tpwsArgumentsMap by lazy {
            getMapForExecType(ExecutableType.Tpws)
        }

        private val nfqsArgumentsMap by lazy {
            getMapForExecType(ExecutableType.Nfqs)
        }

        private val winwsArgumentsMap by lazy {
            getMapForExecType(ExecutableType.Winws)
        }

        private fun parseForTpws(cliKey: String, value: RawValueInput): ZapretArgument<*, *>? {
            val parser = tpwsArgumentsMap[cliKey]?.parser
            return parser?.invoke(value)
        }

        private fun parseForNfqs(cliKey: String, value: RawValueInput): ZapretArgument<*, *>? {
            val parser = nfqsArgumentsMap[cliKey]?.parser
            return parser?.invoke(value)
        }

        private fun parseForWinws(cliKey: String, value: RawValueInput): ZapretArgument<*, *>? {
            val parser = winwsArgumentsMap[cliKey]?.parser
            return parser?.invoke(value)
        }

        fun parse(
            cliKey: String,
            value: RawValueInput,
            executableType: ExecutableType,
        ) = when (executableType) {
            ExecutableType.Winws -> parseForWinws(cliKey, value)
            ExecutableType.Tpws -> parseForTpws(cliKey, value)
            ExecutableType.Nfqs -> parseForNfqs(cliKey, value)
        }
    }
}
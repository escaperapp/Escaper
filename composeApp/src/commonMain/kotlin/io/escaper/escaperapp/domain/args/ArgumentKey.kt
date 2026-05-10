package io.escaper.escaperapp.domain.args

import io.escaper.escaperapp.domain.ExecutableType
import io.escaper.escaperapp.domain.args.tpws.DaemonArgument
import io.escaper.escaperapp.domain.args.tpws.DryRunArgument
import io.escaper.escaperapp.domain.args.tpws.PidFileArgument
import io.escaper.escaperapp.domain.args.tpws.TpwsDebugArgument
import io.escaper.escaperapp.domain.args.tpws.VersionArgument

enum class ArgumentKey(
    val cliKey: String,
    val executableTypes: Set<ExecutableType>,
    val parser: (RawValueInput) -> ZapretArgument<*, *>?
) {
    TpwsDebugModeArg(
        cliKey = "debug",
        parser = { value ->
            TpwsDebugMode.fromCli(value)?.let(::TpwsDebugArgument)
        },
        ExecutableType.Tpws,
    ),
    DryRunArg(
        cliKey = "dry-run",
        parser = { DryRunArgument },
        ExecutableType.Tpws,
        ExecutableType.Nfqs
    ),
    VersionArg(
        cliKey = "version",
        parser = { VersionArgument },
        ExecutableType.Tpws,
        ExecutableType.Nfqs
    ),
    DaemonArg(
        cliKey = "daemon",
        parser = { DaemonArgument },
        ExecutableType.Tpws,
        ExecutableType.Nfqs
    ),
    PidFileArg(
        cliKey = "pidfile",
        parser = { value ->
            PidFileArgument.fromCli(value)?.let(::PidFileArgument)
        },
        ExecutableType.Tpws,
        ExecutableType.Nfqs
    ),

    ;

    constructor(
        cliKey: String,
        parser: (RawValueInput) -> ZapretArgument<*, *>?,
        vararg executableTypes: ExecutableType,
    ) : this(
        cliKey = cliKey,
        parser = parser,
        executableTypes = executableTypes.toSet()
    )

    companion object {
        private fun getMapForExecType(type: ExecutableType) = entries
            .filter { type in it.executableTypes }
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
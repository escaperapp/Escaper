package io.escaper.escaperapp.domain.args

import io.escaper.escaperapp.domain.ExecutableType

enum class ArgumentKey(
    val cliKey: String,
    val executableTypes: Set<ExecutableType>,
    val parser: (RawValueInput) -> AnyZapretArgument?
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
    UserArg(
        cliKey = "user",
        parser = { value ->
            UserArgument.fromCli(value)?.let(::UserArgument)
        },
        ExecutableType.Tpws,
        ExecutableType.Nfqs
    ),
    UidArg(
        cliKey = "uid",
        parser = { value ->
            UidArgumentValue.fromCli(value)?.let(::UidArgument)
        },
        ExecutableType.Tpws,
        ExecutableType.Nfqs
    ),
    BindAddrArg(
        cliKey = "bind-addr",
        parser = { value ->
            value.asString()
                .toStringValue()
                ?.let(::BindAddressArgument)
        },
        ExecutableType.Tpws,
        ExecutableType.Nfqs
    ),
    BindLinkLocalArg(
        cliKey = "bind-linklocal",
        parser = { value ->
            BindLinkLocalMode.fromCli(value)?.let(::BindLinkLocalArgument)
        },
        ExecutableType.Tpws
    ),
    BindIface4Arg(
        cliKey = "bind-iface4",
        parser = { value ->
            value.asString().toStringValue()?.let(::BindInterface4Argument)
        },
        ExecutableType.Tpws
    ),
    BindIface6Arg(
        cliKey = "bind-iface6",
        parser = { value ->
            value.asString().toStringValue()?.let(::BindInterface6Argument)
        },
        ExecutableType.Tpws
    ),
    NewArg(
        cliKey = "new",
        parser = { NewArgument },
        ExecutableType.Tpws,
        ExecutableType.Nfqs,
        ExecutableType.Winws
    )
    ;

    constructor(
        cliKey: String,
        parser: (RawValueInput) -> AnyZapretArgument?,
        vararg executableTypes: ExecutableType,
    ) : this(
        cliKey = cliKey,
        parser = parser,
        executableTypes = executableTypes.toSet()
    )

    companion object {
        private fun getListForExecType(type: ExecutableType) = entries
            .filter { type in it.executableTypes }

        private val tpwsArguments = getListForExecType(ExecutableType.Tpws)
        private val nfqsArguments = getListForExecType(ExecutableType.Nfqs)
        private val winwsArguments = getListForExecType(ExecutableType.Winws)
        private val tpwsArgumentsMap = tpwsArguments.associateBy { it.cliKey }
        private val nfqsArgumentsMap = nfqsArguments.associateBy { it.cliKey }
        private val winwsArgumentsMap = winwsArguments.associateBy { it.cliKey }

        fun getEntriesForExecType(
            executableType: ExecutableType,
        ) = when (executableType) {
            ExecutableType.Winws -> winwsArguments
            ExecutableType.Tpws -> tpwsArguments
            ExecutableType.Nfqs -> nfqsArguments
        }

        private fun parseForTpws(cliKey: String, value: RawValueInput): AnyZapretArgument? {
            val parser = tpwsArgumentsMap[cliKey]?.parser
            return parser?.invoke(value)
        }

        private fun parseForNfqs(cliKey: String, value: RawValueInput): AnyZapretArgument? {
            val parser = nfqsArgumentsMap[cliKey]?.parser
            return parser?.invoke(value)
        }

        private fun parseForWinws(cliKey: String, value: RawValueInput): AnyZapretArgument? {
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
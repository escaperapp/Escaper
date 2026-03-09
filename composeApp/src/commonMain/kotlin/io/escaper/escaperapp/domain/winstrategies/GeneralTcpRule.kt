package io.escaper.escaperapp.domain.winstrategies

internal fun generalTcpRule(
    method: String,
    extraArgs: List<String> = emptyList(),
    resolveListsFile: (String) -> String,
): List<String> {
    return listOf(
        "--filter-tcp=80,443",
        "--hostlist=${resolveListsFile("list-general.txt")}",
        "--dpi-desync=${method}"
    ) + extraArgs
}
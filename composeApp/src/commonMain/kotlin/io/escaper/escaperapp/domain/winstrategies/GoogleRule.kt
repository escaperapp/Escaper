package io.escaper.escaperapp.domain.winstrategies

internal fun googleRule(
    method: String,
    extraArgs: List<String> = emptyList(),
    resolveListsFile: (String) -> String,
): List<String> {
    return listOf(
        "--filter-tcp=443",
        "--hostlist=${resolveListsFile("list-google.txt")}",
        "--ip-id=zero",
        "--dpi-desync=${method}"
    ) + extraArgs + "--new"
}
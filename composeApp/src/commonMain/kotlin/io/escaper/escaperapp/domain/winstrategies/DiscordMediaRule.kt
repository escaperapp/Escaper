package io.escaper.escaperapp.domain.winstrategies

internal fun discordMediaRule(
    method: String,
    extraArgs: List<String> = emptyList(),
): List<String> {
    return listOf(
        "--filter-tcp=2053,2083,2087,2096,8443",
        "--hostlist-domains=discord.media",
        "--dpi-desync=${method}"
    ) + extraArgs + "--new"
}
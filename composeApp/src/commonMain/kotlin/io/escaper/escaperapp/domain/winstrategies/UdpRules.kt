package io.escaper.escaperapp.domain.winstrategies

internal fun udpRules(
    quicRepeats: Int = 6,
    resolveBinFile: (String) -> String,
    resolveListsFile: (String) -> String,
): List<String> = listOf(
    "--filter-udp=443",
    "--hostlist=${resolveListsFile("list-general.txt")}",
    "--dpi-desync=fake",
    "--dpi-desync-repeats=$quicRepeats",
    "--dpi-desync-fake-quic=${resolveBinFile("quic_initial_www_google_com.bin")}",
    "--new",
    "--filter-udp=19294-19344,50000-50100",
    "--filter-l7=discord,stun",
    "--dpi-desync=fake",
    "--dpi-desync-repeats=6",
    "--new"
)
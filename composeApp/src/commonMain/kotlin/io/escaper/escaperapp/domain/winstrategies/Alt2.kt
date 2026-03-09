package io.escaper.escaperapp.domain.winstrategies

fun getAlt2Args(
    alt2Pattern: String,
    resolveBinFile: (String) -> String,
    resolveListsFile: (String) -> String,
): List<String> {
    return listOf(
        wfFull,
        udpRules(
            quicRepeats = 6,
            resolveBinFile = resolveBinFile,
            resolveListsFile = resolveListsFile
        ),
        discordMediaRule(
            method = "multisplit",
            extraArgs = listOf(
                "--dpi-desync-split-seqovl=652",
                "--dpi-desync-split-pos=2",
                "--dpi-desync-split-seqovl-pattern=${alt2Pattern}"
            )
        ),
        googleRule(
            method = "multisplit",
            extraArgs = listOf(
                "--dpi-desync-split-seqovl=652",
                "--dpi-desync-split-pos=2",
                "--dpi-desync-split-seqovl-pattern=${alt2Pattern}"
            ),
            resolveListsFile = resolveListsFile,
        ),
        generalTcpRule(
            method = "multisplit",
            extraArgs = listOf(
                "--dpi-desync-split-seqovl=652",
                "--dpi-desync-split-pos=2",
                "--dpi-desync-split-seqovl-pattern=${alt2Pattern}"
            ),
            resolveListsFile = resolveListsFile
        )
    ).flatten()
}
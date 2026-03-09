package io.escaper.escaperapp.domain

import io.escaper.escaperapp.domain.winstrategies.discordMediaRule
import io.escaper.escaperapp.domain.winstrategies.generalTcpRule
import io.escaper.escaperapp.domain.winstrategies.googleRule
import io.escaper.escaperapp.domain.winstrategies.udpRules
import io.escaper.escaperapp.domain.winstrategies.wfFull

internal class LinuxStrategiesBuilder(
    binDir: String,
    listsDir: String,
) : BaseStrategiesBuilder(binDir = binDir, listsDir = listsDir) {
    override fun buildStrategies(): List<Strategy> {
        return listOf(
            Strategy(
                name = "hostfakesplit",
                args = listOf(
                    wfFull,
                    udpRules(
                        quicRepeats = 6,
                        resolveListsFile = ::resolveListsFile,
                        resolveBinFile = ::resolveBinFile
                    ),

                    discordMediaRule(
                        method = "hostfakesplit",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=4",
                            "--dpi-desync-fooling=ts",
                            "--dpi-desync-hostfakesplit-mod=host=www.google.com"
                        )
                    ),
                    googleRule(
                        method = "hostfakesplit",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=4",
                            "--dpi-desync-fooling=ts",
                            "--dpi-desync-hostfakesplit-mod=host=www.google.com"
                        ),
                        resolveListsFile = ::resolveListsFile
                    ),
                    generalTcpRule(
                        method = "hostfakesplit",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=4",
                            "--dpi-desync-fooling=ts,md5sig",
                            "--dpi-desync-hostfakesplit-mod=host=ozon.ru"
                        ),
                        resolveListsFile = ::resolveListsFile
                    )
                ).flatten()
            ),
            // === BASIC (work on most ISPs) ===
            Strategy(
                name = "split+disorder",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--split-pos=1",
                    "--disorder",
                    "--hostcase"
                )
            ),
            Strategy(
                name = "split-midsld+disorder",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--split-pos=1,midsld",
                    "--disorder",
                    "--hostcase"
                )
            ),
            Strategy(
                name = "split2+disorder",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--split-pos=2",
                    "--disorder",
                    "--hostcase"
                )
            ),
            // === TLS-AWARE ===
            Strategy(
                name = "tlsrec+split+disorder",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--tlsrec=sni",
                    "--split-pos=1",
                    "--disorder",
                    "--hostcase"
                )
            ),
            Strategy(
                name = "split-tls+disorder",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--split-pos=1,midsld",
                    "--disorder=tls",
                    "--hostcase"
                )
            ),
            // === HOST MANIPULATION ===
            Strategy(
                name = "methodeol+split",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--methodeol",
                    "--split-pos=1",
                    "--hostcase"
                )
            ),
            Strategy(
                name = "hostdot+split+disorder",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--hostdot",
                    "--split-pos=1,midsld",
                    "--disorder"
                )
            ),
            Strategy(
                name = "hostpad+split+disorder",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--hostpad=256",
                    "--split-pos=1",
                    "--disorder",
                    "--hostcase"
                )
            ),
            // === OOB ===
            Strategy(
                name = "oob+split+disorder",
                args = listOf("--port", "1080", "--socks", "--oob", "--split-pos=1", "--disorder")
            ),
            Strategy(
                name = "oob+methodeol+split",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--oob",
                    "--methodeol",
                    "--split-pos=1",
                    "--hostcase"
                )
            ),
            // === COMBINED (aggressive) ===
            Strategy(
                name = "combined-v1",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--split-pos=1,midsld",
                    "--disorder",
                    "--hostcase",
                    "--methodeol"
                )
            ),
            Strategy(
                name = "combined-v2",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--oob",
                    "--methodeol",
                    "--split-pos=1,midsld",
                    "--disorder",
                    "--hostcase",
                    "--hostdot"
                )
            ),
            Strategy(
                name = "combined-v3",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--tlsrec=sni",
                    "--hostpad=256",
                    "--split-pos=2",
                    "--disorder",
                    "--hostcase"
                )
            ),
            // === EXTENDED SPLIT POSITIONS ===
            Strategy(
                name = "split3+disorder",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--split-pos=3",
                    "--disorder",
                    "--hostcase"
                )
            ),
            Strategy(
                name = "split-sniext+disorder",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--split-pos=1,sniext",
                    "--disorder",
                    "--hostcase"
                )
            ),
            // === HOST MANIPULATION VARIANTS ===
            Strategy(
                name = "hosttab+split+disorder",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--hosttab",
                    "--split-pos=1",
                    "--disorder",
                    "--hostcase"
                )
            ),
            Strategy(
                name = "hostspell+split",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--hostspell",
                    "--split-pos=1",
                    "--disorder"
                )
            ),
            // === LARGE HOSTPAD VARIANTS ===
            Strategy(
                name = "hostpad512+split+disorder",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--hostpad=512",
                    "--split-pos=1",
                    "--disorder",
                    "--hostcase"
                )
            ),
            Strategy(
                name = "hostpad1024+split",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--hostpad=1024",
                    "--split-pos=1,midsld",
                    "--hostcase"
                )
            ),
            // === TLS RECORD MANIPULATION ===
            Strategy(
                name = "tlsrec+disorder",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--tlsrec=sni",
                    "--disorder",
                    "--hostcase"
                )
            ),
            Strategy(
                name = "tlsrec+oob+split",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--tlsrec=sni",
                    "--oob",
                    "--split-pos=1",
                    "--hostcase"
                )
            ),
            // === AGGRESSIVE COMBINED ===
            Strategy(
                name = "combined-v4",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--oob",
                    "--hostpad=256",
                    "--split-pos=1,midsld",
                    "--disorder",
                    "--hostcase",
                    "--methodeol"
                )
            ),
            Strategy(
                name = "combined-v5",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--tlsrec=sni",
                    "--methodeol",
                    "--hostdot",
                    "--split-pos=2",
                    "--disorder",
                    "--hostcase"
                )
            ),
        )
    }
}
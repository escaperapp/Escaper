package io.escaper.escaperapp.domain

internal class DarwinStrategiesBuilder(
    binDir: String,
    listsDir: String,
) : BaseStrategiesBuilder(binDir = binDir, listsDir = listsDir) {
    override fun buildStrategies(): List<Strategy> {
        return listOf(
            Strategy(
                name = "discord-v3",
                args = listOf(
                    "--port",
                    "1080",
                    "--hostlist=${resolveListsFile("list-discord.txt")}",
                    "--socks",
                    "--tlsrec=sni",
                    "--hostpad=256",
                    "--split-pos=2",
                    "--disorder",
                    "--hostcase",
                    "--new",
                    "--hostlist=${resolveListsFile("list-general.txt")}",
                    "--tlsrec=sni",
                    "--hostpad=512",
                    "--split-pos=1,3",
                    "--disorder",
                    "--split-any-protocol"
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
                name = "combined-v3",
                args = listOf(
                    "--port",
                    "1080",
                    "--socks",
                    "--tlsrec=sni",
                    "--hostpad=256",
                    "--split-pos=2",
                    "--disorder",
                    "--hostcase",
                )
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
                    "--hostspell=abcd",
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
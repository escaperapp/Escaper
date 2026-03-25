package io.escaper.escaperapp.domain

import io.escaper.escaperapp.domain.winstrategies.discordMediaRule
import io.escaper.escaperapp.domain.winstrategies.generalTcpRule
import io.escaper.escaperapp.domain.winstrategies.getAlt2Args
import io.escaper.escaperapp.domain.winstrategies.googleRule
import io.escaper.escaperapp.domain.winstrategies.udpRules
import io.escaper.escaperapp.domain.winstrategies.wfFull

internal class WinStrategiesBuilder(
    binDir: String,
    listsDir: String,
) : BaseStrategiesBuilder(binDir, listsDir) {
    private fun udpRulesWithDiscord443(quicRepeats: Int = 6): List<String> {
        return listOf(
            "--filter-udp=443",
            "--filter-l7=discord,stun",
            "--dpi-desync=fake",
            "--dpi-desync-repeats=6",
            "--new",
        ) + udpRules(
            quicRepeats = quicRepeats,
            resolveBinFile = ::resolveBinFile,
            resolveListsFile = ::resolveListsFile
        )
    }

    private fun discordTcp443Rule(
        method: String,
        extraArgs: List<String> = emptyList(),
    ): List<String> {
        return listOf(
            "--filter-tcp=443", "--hostlist=${resolveListsFile("list-discord.txt")}",
            "--dpi-desync=${method}"
        ) + extraArgs + "--new"
    }

    override fun buildStrategies(): List<Strategy> {

        val alt2Pattern = resolveBinFile("tls_clienthello_www_google_com.bin")

        val alt2Args: List<String> = getAlt2Args(
            alt2Pattern = alt2Pattern,
            resolveBinFile = ::resolveBinFile,
            resolveListsFile = ::resolveListsFile,
        )


        return listOf(
            Strategy(name = "ALT2", args = alt2Args),
            Strategy(
                name = "fake-badseq",
                args = listOf(
                    wfFull,
                    udpRules(
                        quicRepeats = 6,
                        resolveBinFile = ::resolveBinFile,
                        resolveListsFile = ::resolveListsFile
                    ),
                    discordMediaRule(
                        method = "fake",
                        extraArgs = listOf(
                            "--dpi-desync-fake-tls-mod=none", "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=badseq", "--dpi-desync-badseq-increment=2"
                        )
                    ),
                    googleRule(
                        method = "fake",
                        extraArgs = listOf(
                            "--dpi-desync-fake-tls-mod=none", "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=badseq", "--dpi-desync-badseq-increment=2"
                        ),
                        resolveListsFile = ::resolveListsFile,
                    ),
                    generalTcpRule(
                        method = "fake",
                        extraArgs = listOf(
                            "--dpi-desync-fake-tls-mod=none", "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=badseq", "--dpi-desync-badseq-increment=2"
                        ),
                        resolveListsFile = ::resolveListsFile
                    )
                ).flatten()
            ),
            Strategy(
                name = "multisplit-681",
                args = listOf(
                    wfFull,
                    udpRules(
                        quicRepeats = 6,
                        resolveBinFile = ::resolveBinFile,
                        resolveListsFile = ::resolveListsFile
                    ),
                    discordMediaRule(
                        method = "multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-seqovl=681",
                            "--dpi-desync-split-pos=1"
                        )
                    ),
                    googleRule(
                        method = "multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-seqovl=681",
                            "--dpi-desync-split-pos=1"
                        ),
                        resolveListsFile = ::resolveListsFile
                    ),
                    generalTcpRule(
                        method = "multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-seqovl=681",
                            "--dpi-desync-split-pos=1"
                        ),
                        resolveListsFile = ::resolveListsFile
                    )
                ).flatten()
            ),
            Strategy(
                name = "fake+split2-autottl",
                args = listOf(
                    wfFull,
                    udpRules(
                        quicRepeats = 6,
                        resolveBinFile = ::resolveBinFile,
                        resolveListsFile = ::resolveListsFile
                    ),
                    discordMediaRule(
                        method = "fake,split2",
                        extraArgs = listOf(
                            "--dpi-desync-autottl=5", "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=md5sig"
                        )
                    ),
                    googleRule(
                        method = "fake,split2",
                        extraArgs = listOf(
                            "--dpi-desync-autottl=5", "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=md5sig"
                        ),
                        resolveListsFile = ::resolveListsFile,
                    ),
                    generalTcpRule(
                        method = "fake,split2",
                        extraArgs = listOf(
                            "--dpi-desync-autottl=5", "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=md5sig"
                        ),
                        resolveListsFile = ::resolveListsFile
                    )
                ).flatten()
            ),
            Strategy(
                name = "syndata-only", args = listOf(
                    wfFull,
                    udpRules(
                        quicRepeats = 6,
                        resolveBinFile = ::resolveBinFile,
                        resolveListsFile = ::resolveListsFile
                    ),
                    listOf(
                        "--filter-l3=ipv4",
                        "--filter-tcp=443",
                        "--dpi-desync=syndata,multidisorder",
                        "--new",
                        "--filter-tcp=80",
                        "--hostlist=${resolveListsFile("list-general.txt")}",
                        "--dpi-desync=fake", "--dpi-desync-repeats=6"
                    )

                ).flatten()
            ),
            Strategy(
                name = "combo:discord-minimal",
                args = listOf(
                    wfFull,
                    udpRules(
                        quicRepeats = 6,
                        resolveBinFile = ::resolveBinFile,
                        resolveListsFile = ::resolveListsFile
                    ),
                    listOf(
                        "--filter-tcp=2053,2083,2087,2096,8443",
                        "--dpi-desync=fake",
                        "--dpi-desync-repeats=4",
                        "--dpi-desync-fake-tls-mod=none",
                        "--new"
                    ),
                    discordTcp443Rule(
                        "fake",
                        listOf(
                            "--dpi-desync-repeats=4",
                            "--dpi-desync-fake-tls-mod=none"
                        )
                    ),
                    listOf(
                        "--filter-l3=ipv4",
                        "--filter-tcp=443",
                        "--dpi-desync=syndata,multidisorder",
                        "--new",
                        "--filter-tcp=80",
                        "--hostlist=${resolveListsFile("list-general.txt")}",
                        "--dpi-desync=fake",
                        "--dpi-desync-repeats=4",
                        "--dpi-desync-fake-tls-mod=none"
                    ),
                ).flatten()
            ),
            Strategy(
                name = "combo:syndata+badseq",
                args = listOf(
                    wfFull,
                    udpRules(6, ::resolveBinFile, ::resolveListsFile),
                    listOf(
                        "--filter-tcp=2053,2083,2087,2096,8443",
                        "--dpi-desync=fake",
                        "--dpi-desync-repeats=6",
                        "--dpi-desync-fooling=badseq",
                        "--dpi-desync-badseq-increment=2",
                        "--new"
                    ),
                    // Discord TCP 443 FIRST (hostlist) — gentle desync so Discord app works
                    discordTcp443Rule(
                        method = "fake",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=badseq",
                            "--dpi-desync-badseq-increment=2"
                        )
                    ),
                    // All other TCP 443 — syndata+multidisorder (YouTube + everything else)
                    listOf(
                        "--filter-l3=ipv4",
                        "--filter-tcp=443",
                        "--dpi-desync=syndata,multidisorder",
                        "--new",
                        // TCP 80 HTTP fallback
                        "--filter-tcp=80",
                        "--hostlist=${resolveListsFile("list-general.txt")}",
                        "--dpi-desync=fake",
                        "--dpi-desync-repeats=6",
                        "--dpi-desync-fooling=badseq"
                    )
                ).flatten(),
            ),

            // === COMBO #2: Discord-first (multisplit for Discord 443) + syndata (YouTube/rest) ===
            Strategy(
                name = "combo:syndata+multisplit",
                args = listOf(
                    wfFull,
                    udpRules(
                        quicRepeats = 6,
                        resolveListsFile = ::resolveListsFile,
                        resolveBinFile = ::resolveBinFile,
                    ),
                    // Discord media TCP ports — multisplit
                    listOf(
                        "--filter-tcp=2053,2083,2087,2096,8443",
                        "--dpi-desync=multisplit",
                        "--dpi-desync-split-seqovl=681",
                        "--dpi-desync-split-pos=1",
                        "--new"
                    ),
                    // Discord TCP 443 first
                    discordTcp443Rule(
                        method = "multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-seqovl=681",
                            "--dpi-desync-split-pos=1"
                        )
                    ),
                    // All other TCP 443 — syndata
                    listOf(
                        "--filter-l3=ipv4",
                        "--filter-tcp=443",
                        "--dpi-desync=syndata,multidisorder",
                        "--new"
                    ),
                    // TCP 80

                    listOf(
                        "--filter-tcp=80",
                        "--hostlist=${resolveListsFile("list-general.txt")}",
                        "--dpi-desync=multisplit",
                        "--dpi-desync-split-seqovl=681",
                        "--dpi-desync-split-pos=1"
                    )
                ).flatten()
            ),
            // === COMBO #3: Discord-first (fake md5sig for Discord 443) + syndata (YouTube/rest) ===
            Strategy(
                name = "combo:syndata+md5sig",
                args = listOf(
                    wfFull,
                    udpRules(
                        quicRepeats = 6,
                        resolveBinFile = ::resolveBinFile,
                        resolveListsFile = ::resolveListsFile
                    ),
                    // Discord media TCP ports — fake md5sig+ts
                    listOf(
                        "--filter-tcp=2053,2083,2087,2096,8443",
                        "--dpi-desync=fake",
                        "--dpi-desync-repeats=6",
                        "--dpi-desync-fooling=ts,md5sig",
                        "--dpi-desync-fake-tls=${resolveBinFile("tls_clienthello_www_google_com.bin")}",
                        "--new"
                    ),
                    // Discord TCP 443 first
                    discordTcp443Rule(
                        method = "fake",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=ts,md5sig",
                            "--dpi-desync-fake-tls=${resolveBinFile("tls_clienthello_www_google_com.bin")}"
                        )
                    ),
                    listOf(
                        // All other TCP 443 — syndata
                        "--filter-l3=ipv4",
                        "--filter-tcp=443",
                        "--dpi-desync=syndata,multidisorder",
                        "--new",
                        // TCP 80
                        "--filter-tcp=80",
                        "--hostlist=${resolveListsFile("list-general.txt")}",
                        "--dpi-desync=fake",
                        "--dpi-desync-repeats=6",
                        "--dpi-desync-fooling=ts,md5sig"
                    )
                ).flatten()
            ),
            // === COMBO #4: Discord-first (split2 for Discord 443) + syndata (YouTube/rest) ===
            Strategy(
                name = "combo:syndata+split2",
                args = listOf(
                    wfFull,
                    udpRules(
                        quicRepeats = 6,
                        resolveBinFile = ::resolveBinFile,
                        resolveListsFile = ::resolveListsFile
                    ),
                    // Discord media TCP ports — fake+split2 autottl
                    listOf(
                        "--filter-tcp=2053,2083,2087,2096,8443",
                        "--dpi-desync=fake,split2",
                        "--dpi-desync-autottl=5",
                        "--dpi-desync-repeats=6",
                        "--dpi-desync-fooling=md5sig",
                        "--new",
                    ),
                    // Discord TCP 443 first
                    discordTcp443Rule(
                        method = "fake,split2",
                        extraArgs = listOf(
                            "--dpi-desync-autottl=5",
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=md5sig"
                        )
                    ),
                    // All other TCP 443 — syndata
                    listOf(
                        "--filter-l3=ipv4",
                        "--filter-tcp=443",
                        "--dpi-desync=syndata,multidisorder",
                        "--new",
                        // TCP 80
                        "--filter-tcp=80",
                        "--hostlist=${resolveListsFile("list-general.txt")}",
                        "--dpi-desync=fake,split2",
                        "--dpi-desync-autottl=5",
                        "--dpi-desync-repeats=6"
                    )
                ).flatten()
            ),
            // === fake md5sig+ts with TLS pattern files ===
            Strategy(
                name = "fake-md5sig-tls",
                args = listOf(
                    wfFull,
                    udpRules(
                        6,
                        resolveBinFile = ::resolveBinFile,
                        resolveListsFile = ::resolveListsFile
                    ),
                    discordMediaRule(
                        method = "fake",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=ts,md5sig",
                            "--dpi-desync-fake-tls=${resolveBinFile("tls_clienthello_www_google_com.bin")}"
                        )
                    ),
                    googleRule(
                        method = "fake",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=ts,md5sig",
                            "--dpi-desync-fake-tls=${resolveBinFile("tls_clienthello_www_google_com.bin")}"
                        ),
                        resolveListsFile = ::resolveListsFile
                    ),
                    generalTcpRule(
                        method = "fake",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=ts,md5sig",
                            "--dpi-desync-fake-tls=${resolveBinFile("tls_clienthello_www_google_com.bin")}",
                            "--dpi-desync-fake-http=${resolveBinFile("tls_clienthello_max_ru.bin")}"
                        ),
                        resolveListsFile = ::resolveListsFile
                    )
                ).flatten()
            ),
            // === multisplit pos=2,sniext+1 seqovl=679 ===
            Strategy(
                name = "multisplit-679", args = listOf(
                    wfFull,
                    udpRules(
                        6,
                        resolveListsFile = ::resolveListsFile,
                        resolveBinFile = ::resolveBinFile
                    ),
                    discordMediaRule(
                        method = "multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-pos=2,sniext+1",
                            "--dpi-desync-split-seqovl=679"
                        )
                    ),
                    googleRule(
                        method = "multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-pos=2,sniext+1",
                            "--dpi-desync-split-seqovl=679"
                        ),
                        resolveListsFile = ::resolveListsFile
                    ),
                    generalTcpRule(
                        method = "multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-pos=2,sniext+1",
                            "--dpi-desync-split-seqovl=679"
                        ),
                        resolveListsFile = ::resolveListsFile
                    )
                ).flatten()
            ),
            // === #7 fake+multisplit seqovl=664 ts repeats=8 ===

            Strategy(
                name = "fake+multisplit-664",
                args = listOf(
                    wfFull,
                    udpRules(
                        11, resolveListsFile = ::resolveListsFile, resolveBinFile = ::resolveBinFile
                    ),
                    discordMediaRule(
                        method = "fake,multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-seqovl=664",
                            "--dpi-desync-split-pos=1",
                            "--dpi-desync-fooling=ts",
                            "--dpi-desync-repeats=8"
                        )
                    ),
                    googleRule(
                        method = "fake,multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-seqovl=664",
                            "--dpi-desync-split-pos=1",
                            "--dpi-desync-fooling=ts",
                            "--dpi-desync-repeats=8"
                        ),
                        resolveListsFile = ::resolveListsFile
                    ),
                    generalTcpRule(
                        method = "fake,multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-seqovl=664",
                            "--dpi-desync-split-pos=1",
                            "--dpi-desync-fooling=ts",
                            "--dpi-desync-repeats=8"
                        ),
                        resolveListsFile = ::resolveListsFile
                    )
                ).flatten()
            ),
            // === #8 fake+multidisorder badseq+md5sig ===
            Strategy(
                name = "fake+multidisorder",
                args = listOf(
                    wfFull,
                    udpRules(
                        6,
                        resolveListsFile = ::resolveListsFile,
                        resolveBinFile = ::resolveBinFile
                    ),
                    discordMediaRule(
                        method = "fake,multidisorder",
                        extraArgs = listOf(
                            "--dpi-desync-split-pos=1,midsld",
                            "--dpi-desync-fooling=badseq,md5sig"
                        )
                    ),
                    googleRule(
                        method = "fake,multidisorder",
                        extraArgs = listOf(
                            "--dpi-desync-split-pos=1,midsld",
                            "--dpi-desync-fooling=badseq,md5sig"
                        ),
                        resolveListsFile = ::resolveListsFile
                    ),
                    generalTcpRule(
                        method = "fake,multidisorder",
                        extraArgs = listOf(
                            "--dpi-desync-split-pos=1,midsld",
                            "--dpi-desync-fooling=badseq,md5sig"
                        ),
                        resolveListsFile = ::resolveListsFile
                    )
                ).flatten()
            ),
            // === #9 multisplit seqovl=652 pos=2 ===
            Strategy(
                name = "multisplit-652",
                args = listOf(
                    wfFull,
                    udpRules(
                        6, resolveListsFile = ::resolveListsFile, resolveBinFile = ::resolveBinFile
                    ),
                    discordMediaRule(
                        method = "multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-seqovl=652",
                            "--dpi-desync-split-pos=2"
                        )
                    ),
                    googleRule(
                        method = "multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-seqovl=652",
                            "--dpi-desync-split-pos=2"
                        ),
                        resolveListsFile = ::resolveListsFile
                    ),
                    generalTcpRule(
                        method = "multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-seqovl=652",
                            "--dpi-desync-split-pos=2"
                        ),
                        resolveListsFile = ::resolveListsFile
                    )
                ).flatten()
            ),
            // === #10 fake+multisplit badseq increment=1000 ===
            Strategy(
                name = "fake+multisplit-badseq",
                args = listOf(
                    wfFull,
                    udpRules(
                        6,
                        resolveListsFile = ::resolveListsFile,
                        resolveBinFile = ::resolveBinFile
                    ),
                    discordMediaRule(
                        method = "fake,multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=badseq",
                            "--dpi-desync-badseq-increment=1000"
                        )
                    ),
                    googleRule(
                        method = "fake,multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=badseq",
                            "--dpi-desync-badseq-increment=1000"
                        ),
                        resolveListsFile = ::resolveListsFile
                    ),
                    generalTcpRule(
                        method = "fake,multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=badseq",
                            "--dpi-desync-badseq-increment=1000"
                        ),
                        resolveListsFile = ::resolveListsFile
                    )
                ).flatten()
            ),
            // === #11 fake ts with TLS pattern files (simpler) ===
            Strategy(
                name = "fake-ts-tls", args = listOf(
                    wfFull,
                    udpRules(
                        6,
                        resolveListsFile = ::resolveListsFile,
                        resolveBinFile = ::resolveBinFile
                    ),
                    discordMediaRule(
                        method = "fake",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=ts",
                            "--dpi-desync-fake-tls=${resolveBinFile("tls_clienthello_www_google_com.bin")}"
                        )
                    ),
                    googleRule(
                        method = "fake",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=ts",
                            "--dpi-desync-fake-tls=${resolveBinFile("tls_clienthello_www_google_com.bin")}"
                        ),
                        resolveListsFile = ::resolveListsFile
                    ),
                    generalTcpRule(
                        method = "fake",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=ts",
                            "--dpi-desync-fake-tls=${resolveBinFile("tls_clienthello_www_google_com.bin")}",
                            "--dpi-desync-fake-http=${resolveBinFile("tls_clienthello_max_ru.bin")}"
                        ),
                        resolveListsFile = ::resolveListsFile
                    )
                ).flatten()
            ),
            // === #12 general — multisplit seqovl=568 (Flowseal default) ===
            Strategy(
                name = "general",
                args = listOf(
                    wfFull,
                    udpRules(
                        6,
                        resolveListsFile = ::resolveListsFile,
                        resolveBinFile = ::resolveBinFile
                    ),
                    discordMediaRule(
                        method = "multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-seqovl=681",
                            "--dpi-desync-split-pos=1",
                            "--dpi-desync-split-seqovl-pattern=${resolveBinFile("tls_clienthello_www_google_com.bin")}"
                        )
                    ),
                    googleRule(
                        method = "multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-seqovl=681",
                            "--dpi-desync-split-pos=1",
                            "--dpi-desync-split-seqovl-pattern=${resolveBinFile("tls_clienthello_www_google_com.bin")}"
                        ),
                        resolveListsFile = ::resolveListsFile
                    ),
                    generalTcpRule(
                        method = "multisplit",
                        extraArgs = listOf(
                            "--dpi-desync-split-seqovl=568",
                            "--dpi-desync-split-pos=1",
                            "--dpi-desync-split-seqovl-pattern=${resolveBinFile("tls_clienthello_4pda_to.bin")}"
                        ),
                        resolveListsFile = ::resolveListsFile
                    )
                ).flatten()
            ),
            // === #13 fake+fakedsplit ts ===
            Strategy(
                name = "fake+fakedsplit",
                args = listOf(
                    wfFull,
                    udpRules(
                        quicRepeats = 6,
                        resolveListsFile = ::resolveListsFile,
                        resolveBinFile = ::resolveBinFile
                    ),
                    discordMediaRule(
                        method = "fake,fakedsplit",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=ts",
                            "--dpi-desync-fakedsplit-pattern=0x00"
                        )
                    ),
                    googleRule(
                        method = "fake,fakedsplit",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=ts",
                            "--dpi-desync-fakedsplit-pattern=0x00"
                        ),
                        resolveListsFile = ::resolveListsFile
                    ),
                    generalTcpRule(
                        method = "fake,fakedsplit",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=ts",
                            "--dpi-desync-fakedsplit-pattern=0x00"
                        ),
                        resolveListsFile = ::resolveListsFile
                    )
                ).flatten()
            ),
            // === #14 hostfakesplit ts (ALT9-style, works on some ISPs) ===
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
            // === #15 fakedsplit badseq ===

            Strategy(
                name = "fakedsplit-badseq",
                args = listOf(

                    wfFull,
                    udpRules(
                        quicRepeats = 6,
                        resolveBinFile = ::resolveBinFile,
                        resolveListsFile = ::resolveListsFile
                    ),
                    discordMediaRule(
                        method = "fake,fakedsplit",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=badseq",
                            "--dpi-desync-fakedsplit-pattern=0x00"
                        )
                    ),
                    googleRule(
                        method = "fake,fakedsplit",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=badseq",
                            "--dpi-desync-fakedsplit-pattern=0x00"
                        ),
                        resolveListsFile = ::resolveListsFile
                    ),
                    generalTcpRule(
                        method = "fake,fakedsplit",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=badseq",
                            "--dpi-desync-fakedsplit-pattern=0x00"
                        ),
                        resolveListsFile = ::resolveListsFile
                    )
                ).flatten()
            ),
            // === #16 fake ts only (simplest, for lenient ISPs) ===
            Strategy(
                name = "fake-ts",
                args = listOf(
                    wfFull,
                    udpRules(
                        6,
                        resolveBinFile = ::resolveBinFile,
                        resolveListsFile = ::resolveListsFile
                    ),
                    discordMediaRule(
                        method = "fake",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=ts",
                            "--dpi-desync-fake-tls-mod=none"
                        )
                    ),
                    googleRule(
                        method = "fake",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=ts",
                            "--dpi-desync-fake-tls-mod=none"
                        ),
                        resolveListsFile = ::resolveListsFile
                    ),
                    generalTcpRule(
                        method = "fake",
                        extraArgs = listOf(
                            "--dpi-desync-repeats=6",
                            "--dpi-desync-fooling=ts",
                            "--dpi-desync-fake-tls-mod=none"
                        ),
                        resolveListsFile = ::resolveListsFile
                    )
                ).flatten()
            )
        )
    }
}
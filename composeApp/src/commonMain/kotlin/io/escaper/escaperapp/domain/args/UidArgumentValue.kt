package io.escaper.escaperapp.domain.args

data class UidArgumentValue(
    override val rawValue: Pair<Int, Int?>,
) : PairValue<Int, Int?> {
    constructor(
        uid: Int,
        gid: Int?,
    ) : this(uid to gid)

    companion object : ArgValueParser {

        private val Regex = Regex("""(\d+)(?::(\d+))?""")

        override fun fromCli(cliValue: RawValueInput): UidArgumentValue? {
            val raw = cliValue.asString()?.value ?: return null

            val match = Regex.matchEntire(raw) ?: return null

            val uid = match.groupValues[1].toInt()
            val gid = match.groupValues
                .getOrNull(2)
                ?.takeIf { it.isNotBlank() }
                ?.toInt()

            return UidArgumentValue(
                uid = uid,
                gid = gid,
            )
        }
    }
}
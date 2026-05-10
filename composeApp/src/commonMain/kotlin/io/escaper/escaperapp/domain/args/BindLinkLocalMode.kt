package io.escaper.escaperapp.domain.args

enum class BindLinkLocalMode(
    override val rawValue: String,
) : StringValue {

    NO("no"),
    UNWANTED("unwanted"),
    PREFER("prefer"),
    FORCE("force");

    companion object : ArgValueParser {

        override fun fromCli(cliValue: RawValueInput): BindLinkLocalMode? {
            return when (cliValue.asString()?.value) {
                "no" -> NO
                "unwanted" -> UNWANTED
                "prefer" -> PREFER
                "force" -> FORCE
                else -> null
            }
        }
    }
}
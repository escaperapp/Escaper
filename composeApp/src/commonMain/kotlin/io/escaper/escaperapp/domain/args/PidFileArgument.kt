package io.escaper.escaperapp.domain.args

data class PidFileArgument(
    override val value: StringValue,
) : StringArgument<StringValue>(
    name = ArgumentKey.PidFileArg,
    value = value
) {

    companion object : ArgValueParser {
        override fun fromCli(cliValue: RawValueInput): StringValue? {
            return cliValue.asString()?.let {
                StringValue(it.value)
            }
        }
    }
}
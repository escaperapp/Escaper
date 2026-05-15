package io.escaper.escaperapp.domain.args

data class UserArgument(
    override val value: StringValue,
) : StringArgument<StringValue>(
    name = ArgumentKey.UserArg,
    value = value,
) {
    companion object : ArgValueParser {
        override fun fromCli(cliValue: RawValueInput): StringValue? {
            return cliValue.asString()?.let {
                StringValue(it.value)
            }
        }
    }
}
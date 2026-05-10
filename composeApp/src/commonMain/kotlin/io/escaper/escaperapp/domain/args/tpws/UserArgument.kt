package io.escaper.escaperapp.domain.args.tpws

import io.escaper.escaperapp.domain.args.ArgValueParser
import io.escaper.escaperapp.domain.args.ArgumentKey
import io.escaper.escaperapp.domain.args.RawValueInput
import io.escaper.escaperapp.domain.args.StringArgument
import io.escaper.escaperapp.domain.args.StringValue

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
package io.escaper.escaperapp.domain.mappers

import io.escaper.escaperapp.domain.args.AnyZapretArgument
import io.escaper.escaperapp.domain.args.NewArgument
import io.escaper.escaperapp.presentation.editstrategy.GroupOfArguments

internal fun List<GroupOfArguments>.joinToArguments(): List<AnyZapretArgument> {
    return flatMap { it.args + NewArgument }
}
package io.escaper.escaperapp.domain.mappers

import io.escaper.escaperapp.domain.args.AnyZapretArgument
import io.escaper.escaperapp.domain.args.NewArgument
import io.escaper.escaperapp.presentation.editstrategy.GroupOfArguments

internal fun List<AnyZapretArgument>.splitByNew(): List<List<AnyZapretArgument>> {
    val result = mutableListOf<MutableList<AnyZapretArgument>>()
    var current = mutableListOf<AnyZapretArgument>()

    for (arg in this) {
        if (arg == NewArgument) {
            result += current
            current = mutableListOf()
        } else {
            current += arg
        }
    }

    result += current

    return result
}

internal fun List<AnyZapretArgument>.splitToGroupsByNew(): List<GroupOfArguments> {
    return splitByNew().map { GroupOfArguments(it) }
}
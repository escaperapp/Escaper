package io.escaper.escaperapp.domain

import io.escaper.escaperapp.domain.args.AnyZapretArgument

internal data class GroupOfArguments(
    val id: String,
    val indexInStrategy: Int,
    val args: List<AnyZapretArgument>,
)
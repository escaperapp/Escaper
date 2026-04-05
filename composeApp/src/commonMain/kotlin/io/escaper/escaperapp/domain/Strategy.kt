package io.escaper.escaperapp.domain

import io.escaper.escaperapp.domain.args.ZapretArgument
import io.escaper.escaperapp.domain.utils.newUuid

data class Strategy(
    val id: String = newUuid(),
    val name: String,
    @Deprecated("Use zapretArgs")
    val args: List<String>,
    val zapretArgs: List<ZapretArgument<*, *>> = emptyList()
)
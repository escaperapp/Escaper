package io.escaper.escaperapp.domain

import io.escaper.escaperapp.domain.utils.newUuid

data class Strategy(
    val id: String = newUuid(),
    val name: String,
    @Deprecated("Use groups")
    val args: List<String>,
    val groups: List<GroupOfArguments> = emptyList()
)
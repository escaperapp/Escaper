package io.escaper.escaperapp.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class StrategyProgress(
    val current: Int,
    val total: Int,
    val name: String
)
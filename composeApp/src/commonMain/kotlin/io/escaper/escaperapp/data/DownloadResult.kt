package io.escaper.escaperapp.data

data class DownloadResult(
    val success: Boolean,
    val error: String? = null
)
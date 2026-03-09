package io.escaper.escaperapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val autoStart: Boolean = false,
    val autoConnect: Boolean = false,
    val selectedStrategy: String? = null,
    val lastWorkingStrategy: String? = null
) {
    companion object {
        val Default = Settings(
            autoStart = false,
            autoConnect = false,
            selectedStrategy = null,
            lastWorkingStrategy = null,
        )
    }
}

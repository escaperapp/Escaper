package io.escaper.escaperapp.presentation.mainscreen

import androidx.compose.runtime.Composable

internal fun interface SwitchProxyPermissionsCallback {
    fun switchProxy()
}

@Composable
internal expect fun rememberSwitchProxyCallback(
    onSwitch: () -> Unit
): SwitchProxyPermissionsCallback


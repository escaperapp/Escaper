package io.escaper.escaperapp.presentation.mainscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal actual fun rememberSwitchProxyCallback(onSwitch: () -> Unit): SwitchProxyPermissionsCallback {
    return remember(onSwitch) {
        SwitchProxyPermissionsCallback {
            onSwitch()
        }
    }
}
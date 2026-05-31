package io.escaper.escaperapp.presentation.mainscreen

import android.app.Activity
import android.net.VpnService
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
internal actual fun rememberSwitchProxyCallback(
    onSwitch: () -> Unit,
): SwitchProxyPermissionsCallback {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onSwitch()
        }
    }

    return remember(
        launcher,
        onSwitch,
    ) {
        SwitchProxyPermissionsCallback {
            val intent = VpnService.prepare(context)
            if (intent != null) {
                launcher.launch(intent)
            } else {
                onSwitch()
            }
        }
    }
}
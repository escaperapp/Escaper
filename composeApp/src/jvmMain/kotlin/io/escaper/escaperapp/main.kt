package io.escaper.escaperapp

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.escaper_logo_tr
import io.escaper.escaperapp.data.ProxyManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.core.context.startKoin

private val DefaultDesktopWindowSize = DpSize(
    width = 500.dp,
    height = 800.dp
)

@OptIn(DelicateCoroutinesApi::class)
fun main() = application {
    val koinApp = startKoin { installCommonModules() }
    Window(
        state = rememberWindowState(
            size = DefaultDesktopWindowSize,
            position = WindowPosition.Aligned(Alignment.Center)
        ),
        onCloseRequest = {
            GlobalScope.launch {
                koinApp.koin.get<ProxyManager>()
                    .stopProxy()
                exitApplication()
            }
        },
        title = "Escaper",
        icon = painterResource(EscaperRes.drawable.escaper_logo_tr)
    ) {
        App()
    }
}
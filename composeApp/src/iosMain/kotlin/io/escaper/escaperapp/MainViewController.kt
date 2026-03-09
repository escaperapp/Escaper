package io.escaper.escaperapp

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    startKoin { installCommonModules() }
    return ComposeUIViewController { App() }
}
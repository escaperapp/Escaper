package io.escaper.escaperapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController

internal val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController not provided")
}

@Composable
internal fun ProvideNavController(
    controller: NavController,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalNavController provides controller,
        content = content,
    )
}
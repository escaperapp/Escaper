package io.escaper.escaperapp.platform

actual object PlatformProvider {
    internal val jvmPlatform: Platform.JvmPlatform = when {
        System.getProperty("os.name").lowercase().contains("win") -> Platform.Windows
        System.getProperty("os.name").lowercase().contains("mac") -> Platform.MacOS
        else -> Platform.Linux
    }
    actual val platform: Platform = jvmPlatform
}
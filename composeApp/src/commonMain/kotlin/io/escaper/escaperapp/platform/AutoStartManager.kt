package io.escaper.escaperapp.platform

object AutoStartManager {

    fun apply(enabled: Boolean) {
        // Windows: registry Run key
        // macOS: LaunchAgents plist
        // Linux: ~/.config/autostart

        println("AutoStart set to $enabled (not implemented yet)")
    }
}

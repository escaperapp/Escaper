package io.escaper.escaperapp.platform

sealed interface Platform {
    val name: String

    object Ios : Platform {
        override val name: String = "ios"
    }

    object Android : Platform {
        override val name: String = "android"
    }

    sealed interface JvmPlatform : Platform

    object Windows : JvmPlatform {
        override val name: String = "windows"
    }

    object MacOS : JvmPlatform {
        override val name: String = "mac"
    }

    object Linux : JvmPlatform {
        override val name: String = "linux"
    }
}
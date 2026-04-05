package io.escaper.escaperapp.platform

import io.escaper.escaperapp.domain.ExecutableType

sealed interface Platform {
    val name: String

    val executableType: ExecutableType

    object Ios : Platform {
        override val name: String = "ios"
        override val executableType: ExecutableType = ExecutableType.Tpws
    }

    object Android : Platform {
        override val name: String = "android"
        override val executableType: ExecutableType = ExecutableType.Nfqs
    }

    sealed interface JvmPlatform : Platform

    object Windows : JvmPlatform {
        override val name: String = "windows"
        override val executableType: ExecutableType = ExecutableType.Winws
    }

    object MacOS : JvmPlatform {
        override val name: String = "mac"
        override val executableType: ExecutableType = ExecutableType.Tpws
    }

    object Linux : JvmPlatform {
        override val name: String = "linux"
        override val executableType: ExecutableType = ExecutableType.Nfqs
    }
}
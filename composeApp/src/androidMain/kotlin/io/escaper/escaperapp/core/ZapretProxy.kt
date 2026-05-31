package io.escaper.escaperapp.core

import io.escaper.escaperapp.nativebridge.TpwsBridge
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ZapretProxy {

    private val mutex = Mutex()

    suspend fun startProxy(
        args: List<String>,
    ): Int = mutex.withLock {
        TpwsBridge.run(
            args = args.toTypedArray()
        )
    }

    suspend fun stopProxy(): Int {
        return mutex.withLock {
            TpwsBridge.stop()
            0
        }
    }
}
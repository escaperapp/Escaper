package io.escaper.escaperapp.core

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ZapretProxy {
    companion object {
        init {
            // System.loadLibrary("byedpi")
        }
    }

    private val mutex = Mutex()
    private var fd = -1

    suspend fun startProxy(): Int {
        return suspendCancellableCoroutine {

        }
    }

    suspend fun stopProxy(): Int {
        return mutex.withLock {
            // TODO
            0
        }
    }
}
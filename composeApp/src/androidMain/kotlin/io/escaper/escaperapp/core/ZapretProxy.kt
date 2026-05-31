package io.escaper.escaperapp.core

import io.escaper.escaperapp.nativebridge.TpwsBridge
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ZapretProxy {

    private val mutex = Mutex()

    suspend fun startProxy(
        args: List<String>,
        defaultPort: Int,
    ): Int = mutex.withLock {
        val safeArgs = args.ensureRequiredArgs(defaultPort)
        println("Starting tpws with args $safeArgs")
        TpwsBridge.run(
            args = safeArgs.toTypedArray()
        )
    }

    suspend fun stopProxy(): Int {
        return mutex.withLock {
            TpwsBridge.stop()
            0
        }
    }

    private fun List<String>.ensureRequiredArgs(
        defaultPort: Int,
    ): List<String> {
        val port = find { it.startsWith("--port") }
        val portArg = if (port == null) {
            "--port=$defaultPort"
        } else {
            null
        }
        return buildList {
            add("tpws")
            addAll(this@ensureRequiredArgs)
            portArg?.let {
                add(it)
            }
        }
    }
}
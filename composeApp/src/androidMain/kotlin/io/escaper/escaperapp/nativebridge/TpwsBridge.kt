package io.escaper.escaperapp.nativebridge

object TpwsBridge {

    init {
        System.loadLibrary("tpwsjni")
    }

    /**
     * Starts tpws using CLI-style arguments.
     *
     * Example:
     * TpwsBridge.run(arrayOf("--hostlist=...", "--port=8080"))
     *
     * Returns native exit code (0 = OK, non-zero = error).
     */
    external fun run(
        args: Array<String>
    ): Int

    external fun stop()
}
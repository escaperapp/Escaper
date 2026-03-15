package io.escaper.escaperapp.domain

import io.escaper.escaperapp.data.resolveFile

internal abstract class BaseStrategiesBuilder(
    protected val binDir: String,
    protected val listsDir: String,
) {

    protected fun resolveBinFile(file: String): String = resolveFile(
        path = binDir,
        fileName = file
    )

    protected fun resolveListsFile(file: String): String = resolveFile(
        path = listsDir,
        fileName = file
    )

    abstract fun buildStrategies(): List<Strategy>

    companion object {
        val NoOp = object : BaseStrategiesBuilder("", "") {
            override fun buildStrategies(): List<Strategy> = emptyList()
        }
    }
}
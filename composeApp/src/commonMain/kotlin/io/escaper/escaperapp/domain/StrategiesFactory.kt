package io.escaper.escaperapp.domain

import io.escaper.escaperapp.data.HostListsManager
import io.escaper.escaperapp.data.PathsProvider
import io.escaper.escaperapp.data.networking.ensureBinPatternFiles
import io.escaper.escaperapp.platform.Platform
import io.escaper.escaperapp.platform.PlatformProvider
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.Volatile

internal class StrategiesFactory(
    private val pathsProvider: PathsProvider,
    private val hostListsManager: HostListsManager,
) {
    private val mutex = Mutex()

    @Volatile
    private var cachedStrategies: List<Strategy>? = null
    suspend fun getStrategiesForPlatform(): List<Strategy> {
        return mutex.withLock {
            val oldStrategies = cachedStrategies
            if (oldStrategies != null) {
                return@withLock oldStrategies
            }
            val newStrategies = getStrategies()
            cachedStrategies = newStrategies
            return@withLock newStrategies
        }
    }

    private suspend fun getStrategies(): List<Strategy> {
        val binDir = pathsProvider.getExecutableFolder()
        val listsDir = hostListsManager.ensure()
        val builder = getBuilder(binDir = binDir, listsDir = listsDir)
        ensureBinPatternFiles(binDir)
        return builder.buildStrategies()
    }

    private fun getBuilder(binDir: String, listsDir: String): BaseStrategiesBuilder {
        return when (PlatformProvider.platform) {
            Platform.Linux -> LinuxStrategiesBuilder(binDir = binDir, listsDir = listsDir)
            Platform.MacOS -> DarwinStrategiesBuilder(binDir = binDir, listsDir = listsDir)
            Platform.Windows -> WinStrategiesBuilder(binDir = binDir, listsDir = listsDir)
            Platform.Android,
            Platform.Ios,
                -> BaseStrategiesBuilder.NoOp
        }
    }
}
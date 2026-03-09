package io.escaper.escaperapp.data

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import io.escaper.escaperapp.data.MacSystemProxyManager.disableSystemProxy
import io.escaper.escaperapp.data.MacSystemProxyManager.enableSystemProxy
import io.escaper.escaperapp.data.MacSystemProxyManager.getActiveNetworkServices
import io.escaper.escaperapp.domain.ProxyStartResult
import io.escaper.escaperapp.domain.ProxyStopResult
import io.escaper.escaperapp.domain.StrategiesFactory
import io.escaper.escaperapp.platform.Platform
import io.escaper.escaperapp.platform.PlatformProvider
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.file.Files
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

private const val DEFAULT_PROXY_PORT = 1080
private const val DEFAULT_PROXY_HOST = "127.0.0.1"

internal actual class ProxyManager actual constructor(
    private val pathsProvider: PathsProvider,
    private val downloadManager: ExecutableDownloadManager,
    private val settingsRepository: SettingsRepository,
    private val strategiesFactory: StrategiesFactory,
) {
    private val logger = Logger.withTag("ProxyManager")

    private val _state = MutableStateFlow(ProxyManagerState.Initial)
    actual val state = _state.asStateFlow()

    @Volatile
    private var proxyProcess: Process? = null
    private var shutdownHook: Thread? = null

    private val additionalScope = CoroutineScope(Dispatchers.IO)

    actual suspend fun startProxy(): ProxyStartResult {
        return withContext(Dispatchers.IO) {
            if (state.value.isConnected || proxyProcess != null) {
                ProxyStartResult.Error("Already running")
            }

            val binaryPath = pathsProvider.getExecutablePath()

            // Auto download
            if (!Files.exists(binaryPath.asPath())) {
                val downloadResult = downloadManager.downloadAndExtractBinaries()
                if (!downloadResult.success) {
                    return@withContext ProxyStartResult.Error("Failed to download executable binary")
                }
            }

            if (!Files.exists(binaryPath.asPath())) {
                return@withContext ProxyStartResult.Error(
                    "Binary lost after download. Expected at path: $binaryPath"
                )
            }
            logger.i("Successfully downloaded binary to file: $binaryPath")

            if (PlatformProvider.jvmPlatform == Platform.MacOS) {
                val services = getActiveNetworkServices()
                if (services.isEmpty()) {
                    return@withContext ProxyStartResult.Error("Failed to find any network services")
                }
            }

            val allStrategies = strategiesFactory.getStrategiesForPlatform()
            val settings = settingsRepository.getSettings()

            val selectedStrategy = allStrategies.find {
                settings.selectedStrategy == it.name
            } ?: return@withContext ProxyStartResult.Error("Strategy is not provided")


            val args = listOf(binaryPath) + selectedStrategy.args
            logger.i("Starting command with args: $args")
            val processBuilder = ProcessBuilder(args).apply {
                redirectInput(ProcessBuilder.Redirect.PIPE)
                redirectOutput(ProcessBuilder.Redirect.PIPE)
                redirectError(ProcessBuilder.Redirect.PIPE)
            }

            proxyProcess = processBuilder.start()
            createAndApplyShutdownHook()

            additionalScope.launch {
                proxyProcess?.inputStream?.bufferedReader()?.forEachLine {
                    logger.d("Received from proxy stdio: $it")
                }
            }
            additionalScope.launch {
                proxyProcess?.errorStream?.bufferedReader()?.forEachLine {
                    logger.d("Received from proxy stderr: $it")
                }
            }

            // Wait for tpws to start listening
            delay(2.seconds)

            if (proxyProcess?.isAlive != true) {
                return@withContext ProxyStartResult.Error("Proxy process failed to start")
            }

            when (PlatformProvider.jvmPlatform) {
                Platform.Linux -> TODO()
                Platform.MacOS -> {
                    // Quick check: verify tpws is actually listening on port 1080
                    val portOpen = checkPortIsOpen()
                    if (!portOpen) {
                        proxyProcess.shutdown()
                        return@withContext ProxyStartResult.Error(
                            "Proxy failed to start on port $DEFAULT_PROXY_PORT"
                        )
                    }
                    // Enable system SOCKS proxy
                    enableSystemProxy(DEFAULT_PROXY_PORT)
                }
                Platform.Windows -> Unit // On Windows, winws.exe works on driver level directly
            }

            _state.update {
                it.copy(
                    isConnected = true,
                    connectedSince = Clock.System.now()
                        .toLocalDateTime(TimeZone.currentSystemDefault()),
                )
            }
            settingsRepository.updateSettings {
                it.copy(lastWorkingStrategy = selectedStrategy.name)
            }

            return@withContext ProxyStartResult.Success(
                strategy = selectedStrategy
            )
        }
    }

    actual suspend fun stopProxy(): ProxyStopResult {
        return withContext(Dispatchers.IO) {
            // Disable system proxy FIRST
            when (PlatformProvider.jvmPlatform) {
                Platform.Linux -> Unit // TODO
                Platform.MacOS -> disableSystemProxy()
                Platform.Windows -> Unit // TODO stopWinwsMonitor()
            }

            removeAndClearShutdownHook()

            // Kill tracked process
            try {
                proxyProcess.shutdown()
            } catch (_: Exception) {
            } finally {
                proxyProcess = null
            }

            _state.update {
                it.copy(
                    isConnected = false,
                    connectedSince = null,
                )
            }

            ProxyStopResult.Success
        }
    }

    private fun Process?.shutdown() {
        this?.destroy()
        this?.destroyForcibly()
    }

    private fun createAndApplyShutdownHook() {
        val newHook = Thread {
            proxyProcess.shutdown()
        }
        shutdownHook = newHook
        Runtime.getRuntime().addShutdownHook(newHook)
    }

    private fun removeAndClearShutdownHook() {
        shutdownHook?.let {
            Runtime.getRuntime().removeShutdownHook(it)
        }
        shutdownHook = null
    }

    private fun checkPortIsOpen(): Boolean = try {
        Socket().use { socket ->
            socket.connect(
                InetSocketAddress(
                    DEFAULT_PROXY_HOST,
                    DEFAULT_PROXY_PORT
                ),
                2000
            )
            true
        }
    } catch (_: Exception) {
        false
    }
}
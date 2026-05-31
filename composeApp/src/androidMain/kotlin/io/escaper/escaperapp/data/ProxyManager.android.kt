package io.escaper.escaperapp.data

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import io.escaper.escaperapp.domain.ProxyStartResult
import io.escaper.escaperapp.domain.ProxyStopResult
import io.escaper.escaperapp.domain.StrategiesFactory
import io.escaper.escaperapp.service.EscaperVpnService
import io.escaper.escaperapp.service.START_ACTION
import io.escaper.escaperapp.service.STOP_ACTION
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

private const val UNKNOWN_ERROR = "Unknown error"

internal actual class ProxyManager actual constructor(
    private val pathsProvider: PathsProvider,
    private val downloadManager: ExecutableDownloadManager,
    private val settingsRepository: SettingsRepository,
    private val strategiesFactory: StrategiesFactory,
) : KoinComponent {

    private val context: Context by inject()
    private val repository: AndroidConnectionStatusRepository by inject()

    actual val state = repository.connectionState

    actual suspend fun startProxy(): ProxyStartResult {
        if (repository.connectionState.value.isConnected) {
            return ProxyStartResult.Error("Already running")
        }

        return try {
            val allStrategies = strategiesFactory.getStrategiesForPlatform()
            val settings = settingsRepository.getSettings()

            val selectedStrategy = allStrategies.find {
                settings.selectedStrategy == it.name
            } ?: return ProxyStartResult.Error("Strategy is not provided")

            val intent = Intent(context, EscaperVpnService::class.java)
            intent.action = START_ACTION
            ContextCompat.startForegroundService(context, intent)
            withTimeout(5.seconds) {
                repository.connectionState.first { it.isConnected }
                ProxyStartResult.Success(selectedStrategy)
            }
        } catch (_: TimeoutCancellationException) {
            getStartResultError()
        } catch (_: Exception) {
            currentCoroutineContext().ensureActive()
            getStartResultError()
        }
    }

    private fun getStartResultError() = ProxyStartResult.Error(
        repository.errorMessage.value ?: UNKNOWN_ERROR
    )

    actual suspend fun stopProxy(): ProxyStopResult {
        if (!repository.connectionState.value.isConnected) {
            return ProxyStopResult.Error("Already stopped")
        }

        return try {
            val intent = Intent(context, EscaperVpnService::class.java)
            intent.action = STOP_ACTION
            ContextCompat.startForegroundService(context, intent)
            withTimeout(5.seconds) {
                repository.connectionState.first { !it.isConnected }
                ProxyStopResult.Success
            }
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            ProxyStopResult.Error(e.message ?: UNKNOWN_ERROR)
        }
    }
}
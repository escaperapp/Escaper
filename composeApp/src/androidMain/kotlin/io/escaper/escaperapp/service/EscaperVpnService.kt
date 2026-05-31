package io.escaper.escaperapp.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.lifecycle.lifecycleScope
import io.escaper.escaperapp.MainActivity
import io.escaper.escaperapp.R
import io.escaper.escaperapp.core.TProxyService
import io.escaper.escaperapp.core.ZapretProxy
import io.escaper.escaperapp.data.AndroidConnectionStatusRepository
import io.escaper.escaperapp.data.ProxyManagerState
import io.escaper.escaperapp.domain.GetSelectedStrategyUseCase
import io.escaper.escaperapp.domain.Strategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import java.io.File

internal const val START_ACTION = "start"
internal const val STOP_ACTION = "stop"

private const val DEFAULT_PORT = 1080

class EscaperVpnService : LifecycleVpnService(), KoinComponent {
    private var proxyJob: Job? = null
    private var tunFd: ParcelFileDescriptor? = null
    private val mutex = Mutex()
    private var stopping: Boolean = false

    private val repository: AndroidConnectionStatusRepository by inject()

    private val zapretProxy: ZapretProxy by inject()

    private val getSelectedStrategy: GetSelectedStrategyUseCase by inject()

    private val status
        get() = repository.connectionState.value

    override fun onCreate() {
        super.onCreate()
        registerNotificationChannel(
            context = this,
            id = NOTIFICATION_CHANNEL_ID,
            name = R.string.vpn_channel_name,
        )
    }

    private fun doAsync(
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return lifecycleScope.launch(Dispatchers.IO) {
            block()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return when (val action = intent?.action) {
            START_ACTION -> {
                doAsync { start() }
                START_STICKY
            }

            STOP_ACTION -> {
                doAsync { stop() }
                START_NOT_STICKY
            }

            else -> {
                Log.w(TAG, "Unknown action: $action")
                START_NOT_STICKY
            }
        }
    }

    override fun onRevoke() {
        Log.i(TAG, "VPN revoked")
        doAsync { stop() }
    }

    private suspend fun start() {
        Log.i(TAG, "Starting")

        if (status.isConnected) {
            Log.w(TAG, "VPN already connected")
            return
        }

        val strategy = getSelectedStrategy()
        if (strategy == null) {
            val msg = "No selected strategy found"
            Log.e(TAG, msg)
            updateStatus(
                newStatus = ProxyManagerState.Disconnected,
                errorMessage = msg
            )
            return
        }

        try {
            mutex.withLock {
                startProxy(strategy)
                startTun2Socks()
            }
            updateStatus(
                newStatus = ProxyManagerState.connected(),
                errorMessage = null
            )
            startForeground()
        } catch (e: Exception) {
            val msg = "Failed to start VPN"
            Log.e(TAG, msg, e)
            updateStatus(
                newStatus = ProxyManagerState.Disconnected,
                errorMessage = msg
            )
            stop()
        }
    }

    private fun startForeground() {
        val notification: Notification = createNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                FOREGROUND_SERVICE_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE,
            )
        } else {
            startForeground(FOREGROUND_SERVICE_ID, notification)
        }
    }

    private suspend fun stop() {
        Log.i(TAG, "Stopping")

        mutex.withLock {
            stopping = true
            try {
                stopTun2Socks()
                stopProxy()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to stop VPN", e)
            } finally {
                stopping = false
            }
        }
        Log.i(TAG, "Stopped tunnel and proxy")
        updateStatus(
            newStatus = ProxyManagerState.Disconnected,
            errorMessage = null
        )
        stopSelf()
    }

    private fun startProxy(strategy: Strategy) {
        Log.i(TAG, "Starting proxy")

        if (proxyJob != null) {
            Log.w(TAG, "Proxy is already starting")
            return
        }

        proxyJob = doAsync {
            val code = zapretProxy.startProxy(
                args = strategy.args,
                defaultPort = DEFAULT_PORT
            )

            if (code != 0) {
                val msg = "Failed to start proxy, exit code $code"
                Log.e(TAG, msg)
                updateStatus(
                    newStatus = ProxyManagerState.Disconnected,
                    errorMessage = msg
                )
            }
        }

        Log.i(TAG, "Proxy started")
    }

    private suspend fun stopProxy() {
        Log.i(TAG, "Stopping proxy")

        if (!status.isConnected) {
            Log.w(TAG, "Proxy already disconnected")
            return
        }

        zapretProxy.stopProxy()
        proxyJob?.cancelAndJoin() ?: throw IllegalStateException("ProxyJob field null")
        proxyJob = null

        Log.i(TAG, "Proxy stopped")
    }

    private fun startTun2Socks() {
        Log.i(TAG, "Starting tun2socks")

        if (tunFd != null) {
            throw IllegalStateException("VPN field not null")
        }

        val dns = "1.1.1.1"
        val ipv6 = false

        val tun2socksConfig = """
        | misc:
        |   task-stack-size: 81920
        | socks5:
        |   mtu: 8500
        |   address: 127.0.0.1
        |   port: $DEFAULT_PORT
        |   udp: udp
        """.trimMargin("| ")

        val configPath = try {
            File.createTempFile("config", "tmp", cacheDir).apply {
                writeText(tun2socksConfig)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create config file", e)
            throw e
        }

        val fd = createBuilder(dns, ipv6).establish()
            ?: throw IllegalStateException("VPN connection failed")

        this.tunFd = fd

        TProxyService.TProxyStartService(configPath.absolutePath, fd.fd)

        Log.i(TAG, "Tun2Socks started")
    }

    private fun stopTun2Socks() {
        Log.i(TAG, "Stopping tun2socks")

        TProxyService.TProxyStopService()

        try {
            File(cacheDir, "config.tmp").delete()
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to delete config file", e)
        }

        tunFd?.close() ?: Log.w(TAG, "VPN not running")
        tunFd = null

        Log.i(TAG, "Tun2socks stopped")
    }

    private fun updateStatus(
        newStatus: ProxyManagerState,
        errorMessage: String?,
    ) {
        repository.updateState(newStatus)
        repository.updateErrorMessage(errorMessage)
    }

    private fun createNotification(): Notification =
        createConnectionNotification(
            this,
            NOTIFICATION_CHANNEL_ID,
            R.string.notification_title,
            R.string.vpn_notification_content,
            EscaperVpnService::class.java,
        )

    private fun createBuilder(dns: String, ipv6: Boolean): Builder {
        Log.d(TAG, "DNS: $dns")
        val builder = Builder()
        builder.setSession("ByeDPI")
        builder.setConfigureIntent(
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE,
            )
        )

        builder.addAddress("10.10.10.10", 32)
            .addRoute("0.0.0.0", 0)

        if (ipv6) {
            builder.addAddress("fd00::1", 128)
                .addRoute("::", 0)
        }

        if (dns.isNotBlank()) {
            builder.addDnsServer(dns)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            builder.setMetered(false)
        }

        builder.addDisallowedApplication(applicationContext.packageName)

        return builder
    }

    private companion object {
        const val TAG: String = "EscaperVpnService"
        const val FOREGROUND_SERVICE_ID: Int = 1
        const val NOTIFICATION_CHANNEL_ID: String = "ByeDPIVpn"
    }
}

package io.escaper.escaperapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.escaper.escaperapp.platform.Platform
import io.escaper.escaperapp.platform.PlatformProvider
import java.io.BufferedReader
import java.nio.charset.StandardCharsets

object MacSystemProxyManager {

    private val proxyEnabledServices = mutableSetOf<String>()

    private fun isMac(): Boolean {
        return PlatformProvider.jvmPlatform == Platform.MacOS
    }

    private fun exec(command: List<String>): String {
        val process = ProcessBuilder(command)
            .redirectErrorStream(true)
            .start()

        val output = process.inputStream
            .bufferedReader(StandardCharsets.UTF_8)
            .use(BufferedReader::readText)

        process.waitFor()
        return output
    }

    suspend fun getActiveNetworkServices(): List<String> {
        if (!isMac()) return emptyList()

        return withContext(Dispatchers.IO) {
            try {
                val output = exec(
                    listOf("networksetup", "-listallnetworkservices")
                )

                val allServices = output
                    .lineSequence()
                    .map { it.trim() }
                    .filter { it.isNotEmpty() && !it.startsWith("An asterisk") }
                    .toList()

                val active = mutableListOf<String>()

                for (service in allServices) {
                    runCatching {
                        val info = exec(
                            listOf("networksetup", "-getinfo", service)
                        )

                        val hasIp = Regex(
                            """IP address:\s*\d+\.\d+\.\d+\.\d+"""
                        ).containsMatchIn(info)

                        if (hasIp) {
                            active += service
                        }
                    }
                }

                active.ifEmpty {
                    allServices.filter {
                        Regex("wi-fi|ethernet|usb", RegexOption.IGNORE_CASE)
                            .containsMatchIn(it)
                    }
                }
            } catch (_: Exception) {
                listOf("Wi-Fi")
            }
        }
    }

   suspend fun enableSystemProxy(port: Int = 1080) {
        if (!isMac()) return

       withContext(Dispatchers.IO) {
           val services = getActiveNetworkServices()
           proxyEnabledServices.clear()

           for (service in services) {
               runCatching {
                   exec(
                       listOf(
                           "networksetup",
                           "-setsocksfirewallproxy",
                           service,
                           "127.0.0.1",
                           port.toString()
                       )
                   )
                   exec(
                       listOf(
                           "networksetup",
                           "-setsocksfirewallproxystate",
                           service,
                           "on"
                       )
                   )
                   proxyEnabledServices += service
               }
           }
       }
    }

    suspend fun disableSystemProxy() {
        if (!isMac()) return

        withContext(Dispatchers.IO) {
            val services = (proxyEnabledServices + getActiveNetworkServices()).toSet()

            for (service in services) {
                runCatching {
                    exec(
                        listOf(
                            "networksetup",
                            "-setsocksfirewallproxystate",
                            service,
                            "off"
                        )
                    )
                }
            }

            proxyEnabledServices.clear()
        }
    }
}

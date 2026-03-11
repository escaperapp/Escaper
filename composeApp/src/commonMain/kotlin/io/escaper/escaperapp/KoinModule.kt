package io.escaper.escaperapp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.escaper.escaperapp.data.DataStoreSettingsRepository
import io.escaper.escaperapp.data.ExecutableDownloadManager
import io.escaper.escaperapp.data.HostListsManager
import io.escaper.escaperapp.data.KtorKermitLogger
import io.escaper.escaperapp.data.PathsProvider
import io.escaper.escaperapp.data.ProxyManager
import io.escaper.escaperapp.data.SettingsRepository
import io.escaper.escaperapp.data.ZapretUrlProvider
import io.escaper.escaperapp.data.ZipExtractor
import io.escaper.escaperapp.data.createDataStore
import io.escaper.escaperapp.domain.StrategiesFactory
import io.escaper.escaperapp.presentation.mainscreen.MainScreenViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun KoinApplication.installCommonModules() {
    modules(
        module {
            single { PathsProvider() }
            single { ZipExtractor() }
            single { HostListsManager() }
            single {
                StrategiesFactory(
                    pathsProvider = get(),
                    hostListsManager = get()
                )
            }
            single<Json> {
                Json {
                    ignoreUnknownKeys = true
                }
            }
            single<HttpClient> {
                HttpClient(CIO) {

                    followRedirects = false

                    install(HttpTimeout) {
                        requestTimeoutMillis = 60_000
                        connectTimeoutMillis = 30_000
                        socketTimeoutMillis = 60_000
                    }
                    install(Logging) {
                        // TODO: Disable logging in relese builds
                        level = LogLevel.ALL
                        logger = KtorKermitLogger()
                    }

                    expectSuccess = false
                }
            }
            single<DataStore<Preferences>> {
                createDataStore()
            }
            single<SettingsRepository> {
                DataStoreSettingsRepository(
                    dataStore = get()
                )
            }
            single {
                ZapretUrlProvider(
                    httpClient = get(),
                    json = get()
                )
            }
            single {
                ExecutableDownloadManager(
                    pathsProvider = get(),
                    httpClient = get(),
                    zapretUrlProvider = get(),
                    zipExtractor = get()
                )
            }
            single {
                ProxyManager(
                    pathsProvider = get(),
                    downloadManager = get(),
                    settingsRepository = get(),
                    strategiesFactory = get()
                )
            }
            viewModel {
                MainScreenViewModel(
                    proxyManager = get(),
                    settingsRepository = get(),
                    downloadManager = get(),
                    strategiesFactory = get(),
                )
            }
        }
    )
}
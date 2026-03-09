package io.escaper.escaperapp.data

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.StateFlow

internal expect class ExecutableDownloadManager(
    pathsProvider: PathsProvider,
    httpClient: HttpClient,
    zapretUrlProvider: ZapretUrlProvider,
    zipExtractor: ZipExtractor,
) {
    val isDownloading: StateFlow<Boolean>

    suspend fun downloadAndExtractBinaries(): DownloadResult
}
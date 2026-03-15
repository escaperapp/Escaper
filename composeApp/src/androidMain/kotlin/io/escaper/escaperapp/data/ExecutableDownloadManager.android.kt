package io.escaper.escaperapp.data

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal actual class ExecutableDownloadManager actual constructor(
    pathsProvider: PathsProvider,
    httpClient: HttpClient,
    zapretUrlProvider: ZapretUrlProvider,
    zipExtractor: ZipExtractor,
) {
    private val _isDownloading = MutableStateFlow(false)
    actual val isDownloading = _isDownloading.asStateFlow()

    actual suspend fun downloadAndExtractBinaries(): DownloadResult {
        // TODO("Not yet implemented")
        return DownloadResult(false, null)
    }
}
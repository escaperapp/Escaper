package io.escaper.escaperapp.data

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.StateFlow

internal actual class ExecutableDownloadManager actual constructor(
    pathsProvider: PathsProvider,
    httpClient: HttpClient,
    zapretUrlProvider: ZapretUrlProvider,
    zipExtractor: ZipExtractor,
) {
    actual val isDownloading: StateFlow<Boolean>
        get() = TODO("Not yet implemented")

    actual suspend fun downloadAndExtractBinaries(): DownloadResult {
        TODO("Not yet implemented")
    }
}
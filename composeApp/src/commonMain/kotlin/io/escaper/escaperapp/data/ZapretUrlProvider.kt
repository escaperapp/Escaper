package io.escaper.escaperapp.data

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.utils.io.charsets.Charsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import io.escaper.escaperapp.APP_NAME

class ZapretUrlProvider(
    private val httpClient: HttpClient,
    private val json: Json,
) {
    suspend fun getLatestZapretUrl(): String {
        return withContext(Dispatchers.IO) {
            val response: HttpResponse = httpClient.get(
                "https://api.github.com/repos/bol-van/zapret/releases/latest"
            ) {
                header(HttpHeaders.UserAgent, APP_NAME)
            }

            if (!response.status.isSuccess()) {
                error("Failed to get zapret url")
            }
            val respString = response.bodyAsText(Charsets.UTF_8)
            val release = json.decodeFromString<ZapretRelease>(respString)

            findZipAsset(release)
        }
    }

    private fun findZipAsset(release: ZapretRelease): String {
        return release.assets
            .find { it.name.endsWith(".zip") }
            ?.browserDownloadUrl
            ?: error("ZIP asset not found")
    }

    @Serializable
    private class ZapretRelease(
        val assets: List<GitHubAsset>
    )

    @Serializable
    data class GitHubAsset(
        val name: String,
        @SerialName("browser_download_url")
        val browserDownloadUrl: String
    )
}
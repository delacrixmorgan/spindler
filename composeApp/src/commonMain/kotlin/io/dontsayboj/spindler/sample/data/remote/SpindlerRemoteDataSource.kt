package io.dontsayboj.spindler.sample.data.remote

import io.dontsayboj.spindler.sample.data.mapper.GedcomIndexDtoToModelMapper
import io.dontsayboj.spindler.sample.domain.model.GedcomIndex
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders

object SpindlerRemoteDataSource {

    private val httpClient = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = 10_000
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }

        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 2)
            exponentialDelay()
        }
    }

    private val gedcomIndexDtoToModelMapper: GedcomIndexDtoToModelMapper by lazy { GedcomIndexDtoToModelMapper() }

    suspend fun loadData(url: String, headers: Map<String, String>? = null): GedcomIndex {
        try {
            val gedcomContent = httpClient.get(url) {
                headers {
                    append(HttpHeaders.Accept, "text/plain, text/gedcom, application/octet-stream, */*")
                    headers?.forEach { (key, value) ->
                        append(key, value)
                    }
                }
            }.body<String>()

            return gedcomIndexDtoToModelMapper(gedcomContent)
        } catch (e: Exception) {
            throw RemoteDataSourceException("Failed to download or parse GEDCOM file from $url", e)
        } finally {
            close()
        }
    }

    private fun close() {
        httpClient.close()
    }
}

class RemoteDataSourceException(message: String, cause: Throwable? = null) : Exception(message, cause)

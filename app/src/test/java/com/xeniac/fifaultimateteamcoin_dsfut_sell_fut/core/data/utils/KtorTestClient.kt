package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createKtorTestClient(
    mockEngine: MockEngine,
    json: Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        coerceInputValues = true
    }
): HttpClient = HttpClient(engine = mockEngine) {
    install(ContentNegotiation) {
        json(json)
    }
    install(DefaultRequest) {
        contentType(ContentType.Application.Json)
    }
}
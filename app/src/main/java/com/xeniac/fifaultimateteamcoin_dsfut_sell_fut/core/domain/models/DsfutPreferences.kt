package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class DsfutPreferences(
    val partnerId: String? = null,
    val secretKey: String? = null,
    val selectedPlatform: Platform = Platform.CONSOLE
)

object DsfutPreferencesSerializer : Serializer<DsfutPreferences> {

    override val defaultValue: DsfutPreferences = DsfutPreferences()

    override suspend fun readFrom(input: InputStream): DsfutPreferences {
        try {
            return Json.decodeFromString(
                deserializer = DsfutPreferences.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            throw CorruptionException(
                message = "Unable to read DsfutPreferences",
                cause = e
            )
        }
    }

    override suspend fun writeTo(t: DsfutPreferences, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = DsfutPreferences.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}
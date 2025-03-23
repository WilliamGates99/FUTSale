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
data class MiscellaneousPreferences(
    val appUpdateDialogShowCount: Int = 0,
    val appUpdateDialogShowEpochDays: Int? = null,
    val selectedRateAppOption: RateAppOption = RateAppOption.NOT_SHOWN_YET,
    val previousRateAppRequestTimeInMs: Long? = null
)

object MiscellaneousPreferencesSerializer : Serializer<MiscellaneousPreferences> {

    override val defaultValue: MiscellaneousPreferences = MiscellaneousPreferences()

    override suspend fun readFrom(input: InputStream): MiscellaneousPreferences {
        try {
            return Json.decodeFromString(
                deserializer = MiscellaneousPreferences.serializer(),
                string = input.use { it.readBytes() }.decodeToString()
            )
        } catch (e: SerializationException) {
            throw CorruptionException(
                message = "Unable to read MiscellaneousPreferences",
                cause = e
            )
        }
    }

    override suspend fun writeTo(
        t: MiscellaneousPreferences,
        output: OutputStream
    ) {
        withContext(Dispatchers.IO) {
            output.use {
                it.write(
                    Json.encodeToString(
                        serializer = MiscellaneousPreferences.serializer(),
                        value = t
                    ).encodeToByteArray()
                )
            }
        }
    }
}
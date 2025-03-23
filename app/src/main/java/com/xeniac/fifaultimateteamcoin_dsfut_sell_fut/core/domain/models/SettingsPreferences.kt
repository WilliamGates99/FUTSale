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
data class SettingsPreferences(
    val isOnboardingCompleted: Boolean = false,
    val notificationPermissionCount: Int = 0,
    val themeIndex: Int = AppTheme.Default.index,
    val isNotificationSoundEnabled: Boolean = true,
    val isNotificationVibrateEnabled: Boolean = true
)

object SettingsPreferencesSerializer : Serializer<SettingsPreferences> {

    override val defaultValue: SettingsPreferences = SettingsPreferences()

    override suspend fun readFrom(input: InputStream): SettingsPreferences {
        try {
            return Json.decodeFromString(
                deserializer = SettingsPreferences.serializer(),
                string = input.use { it.readBytes() }.decodeToString()
            )
        } catch (e: SerializationException) {
            throw CorruptionException(
                message = "Unable to read SettingsPreferences",
                cause = e
            )
        }
    }

    override suspend fun writeTo(
        t: SettingsPreferences,
        output: OutputStream
    ) {
        withContext(Dispatchers.IO) {
            output.use {
                it.write(
                    Json.encodeToString(
                        serializer = SettingsPreferences.serializer(),
                        value = t
                    ).encodeToByteArray()
                )
            }
        }
    }
}

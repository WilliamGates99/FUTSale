package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DATASTORE_IS_NOTIFICATION_SOUND_ACTIVE_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DATASTORE_IS_NOTIFICATION_VIBRATE_ACTIVE_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DATASTORE_IS_ONBOARDING_COMPLETED_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DATASTORE_PARTNER_ID_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DATASTORE_PREVIOUS_REQUEST_TIME_IN_MILLIS_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DATASTORE_RATE_APP_DIALOG_CHOICE_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DATASTORE_SECRET_KEY_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DATASTORE_THEME_KEY
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class PreferencesRepositoryImp @Inject constructor(
    private val settingsDataStore: DataStore<Preferences>
) : PreferencesRepository {

    private object PreferencesKeys {
        val IS_ONBOARDING_COMPLETED = booleanPreferencesKey(DATASTORE_IS_ONBOARDING_COMPLETED_KEY)
        val CURRENT_APP_THEME = intPreferencesKey(DATASTORE_THEME_KEY)
        val IS_NOTIFICATION_SOUND_ACTIVE = booleanPreferencesKey(
            DATASTORE_IS_NOTIFICATION_SOUND_ACTIVE_KEY
        )
        val IS_NOTIFICATION_VIBRATE_ACTIVE = booleanPreferencesKey(
            DATASTORE_IS_NOTIFICATION_VIBRATE_ACTIVE_KEY
        )
        val RATE_APP_DIALOG_CHOICE = intPreferencesKey(DATASTORE_RATE_APP_DIALOG_CHOICE_KEY)
        val PREVIOUS_REQUEST_TIME_IN_MILLIS = longPreferencesKey(
            DATASTORE_PREVIOUS_REQUEST_TIME_IN_MILLIS_KEY
        )
        val PARTNER_ID = stringPreferencesKey(DATASTORE_PARTNER_ID_KEY)
        val SECRET_KEY = stringPreferencesKey(DATASTORE_SECRET_KEY_KEY)
    }

    override fun getCurrentAppThemeSynchronously(): Int = runBlocking {
        try {
            settingsDataStore.data.first()[PreferencesKeys.CURRENT_APP_THEME] ?: 0
        } catch (e: Exception) {
            Timber.e("getCurrentAppThemeSynchronously Exception: $e")
            0
        }
    }

    override suspend fun isOnBoardingCompleted(): Boolean = try {
        settingsDataStore.data.first()[PreferencesKeys.IS_ONBOARDING_COMPLETED] ?: false
    } catch (e: Exception) {
        Timber.e("isOnBoardingCompleted Exception: $e")
        false
    }

    override suspend fun getCurrentAppTheme(): Int = try {
        settingsDataStore.data.first()[PreferencesKeys.CURRENT_APP_THEME] ?: 0
    } catch (e: Exception) {
        Timber.e("getCurrentAppTheme Exception: $e")
        0
    }

    override suspend fun isNotificationSoundActive(): Boolean = try {
        settingsDataStore.data.first()[PreferencesKeys.IS_NOTIFICATION_SOUND_ACTIVE] ?: true
    } catch (e: Exception) {
        Timber.e("isNotificationSoundActive Exception: $e")
        true
    }

    override suspend fun isNotificationVibrateActive(): Boolean = try {
        settingsDataStore.data.first()[PreferencesKeys.IS_NOTIFICATION_VIBRATE_ACTIVE] ?: true
    } catch (e: Exception) {
        Timber.e("isNotificationVibrateActive Exception: $e")
        true
    }

    override suspend fun getRateAppDialogChoice(): Int = try {
        settingsDataStore.data.first()[PreferencesKeys.RATE_APP_DIALOG_CHOICE] ?: 0
    } catch (e: Exception) {
        Timber.e("getRateAppDialogChoice Exception: $e")
        0
    }

    override suspend fun getPreviousRequestTimeInMillis(): Long = try {
        settingsDataStore.data.first()[PreferencesKeys.PREVIOUS_REQUEST_TIME_IN_MILLIS] ?: 0L
    } catch (e: Exception) {
        Timber.e("getPreviousRequestTimeInMillis Exception: $e")
        0L
    }

    override suspend fun getPartnerId(): String? = try {
        settingsDataStore.data.first()[PreferencesKeys.PARTNER_ID]
    } catch (e: Exception) {
        Timber.e("getPartnerId Exception: $e")
        null
    }

    override suspend fun getSecretKey(): String? = try {
        settingsDataStore.data.first()[PreferencesKeys.SECRET_KEY]
    } catch (e: Exception) {
        Timber.e("getSecretKey Exception: $e")
        null
    }

    override suspend fun isOnBoardingCompleted(isCompleted: Boolean) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.IS_ONBOARDING_COMPLETED] = isCompleted
                Timber.i("isOnBoardingCompleted edited to $isCompleted")
            }
        } catch (e: Exception) {
            Timber.e("isOnBoardingCompleted Exception: $e")
        }
    }

    override suspend fun setCurrentAppTheme(index: Int) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.CURRENT_APP_THEME] = index
                Timber.i("AppTheme edited to $index")
            }
        } catch (e: Exception) {
            Timber.e("setCurrentAppTheme Exception: $e")
        }
    }

    override suspend fun isNotificationSoundActive(isActive: Boolean) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.IS_NOTIFICATION_SOUND_ACTIVE] = isActive
                Timber.i("isNotificationSoundActive edited to $isActive")
            }
        } catch (e: Exception) {
            Timber.e("isNotificationSoundActive Exception: $e")
        }
    }

    override suspend fun isNotificationVibrateActive(isActive: Boolean) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.IS_NOTIFICATION_VIBRATE_ACTIVE] = isActive
                Timber.i("isNotificationVibrateActive edited to $isActive")
            }
        } catch (e: Exception) {
            Timber.e("isNotificationVibrateActive Exception: $e")
        }
    }

    override suspend fun setRateAppDialogChoice(value: Int) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.RATE_APP_DIALOG_CHOICE] = value
                Timber.i("RateAppDialogChoice edited to $value")
            }
        } catch (e: Exception) {
            Timber.e("setRateAppDialogChoice Exception: $e")
        }
    }

    override suspend fun setPreviousRequestTimeInMillis(timeInMillis: Long) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.PREVIOUS_REQUEST_TIME_IN_MILLIS] = timeInMillis
                Timber.i("PreviousRequestTimeInMillis edited to $timeInMillis")
            }
        } catch (e: Exception) {
            Timber.e("setPreviousRequestTimeInMillis Exception: $e")
        }
    }

    override suspend fun setPartnerId(partnerId: String?) {
        try {
            settingsDataStore.edit {
                if (partnerId.isNullOrBlank()) {
                    it.remove(PreferencesKeys.PARTNER_ID)
                    Timber.i("PartnerId removed")
                } else {
                    it[PreferencesKeys.PARTNER_ID] = partnerId
                    Timber.i("PartnerId edited to $partnerId")
                }
            }
        } catch (e: Exception) {
            Timber.e("setPartnerId Exception: $e")
        }
    }

    override suspend fun setSecretKey(secretKey: String?) {
        try {
            settingsDataStore.edit {
                if (secretKey.isNullOrBlank()) {
                    it.remove(PreferencesKeys.SECRET_KEY)
                    Timber.i("SecretKey removed")
                } else {
                    it[PreferencesKeys.SECRET_KEY] = secretKey
                    Timber.i("SecretKey edited to $secretKey")
                }
            }
        } catch (e: Exception) {
            Timber.e("setSecretKey Exception: $e")
        }
    }
}
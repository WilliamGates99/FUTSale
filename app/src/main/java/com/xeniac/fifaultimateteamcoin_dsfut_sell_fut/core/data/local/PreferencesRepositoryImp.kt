package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.text.layoutDirection
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto.AppLocaleDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto.AppThemeDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toAppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toAppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Constants.SELECTED_PLATFORM_CONSOLE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class PreferencesRepositoryImp @Inject constructor(
    private val settingsDataStore: DataStore<Preferences>
) : PreferencesRepository {

    private object PreferencesKeys {
        val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("isOnBoardingCompleted")
        val NOTIFICATION_PERMISSION_COUNT = intPreferencesKey(name = "notificationPermissionCount")
        val CURRENT_APP_THEME = intPreferencesKey("theme")
        val IS_NOTIFICATION_SOUND_ENABLED = booleanPreferencesKey("isNotificationSoundActive")
        val IS_NOTIFICATION_VIBRATE_ENABLED = booleanPreferencesKey("isNotificationVibrateActive")
        val RATE_APP_DIALOG_CHOICE = intPreferencesKey("rateAppDialogChoice")
        val PREVIOUS_REQUEST_TIME_IN_MILLIS = longPreferencesKey("previousRequestTimeInMillis")
        val PARTNER_ID = stringPreferencesKey("partnerId")
        val SECRET_KEY = stringPreferencesKey("secretKey")
        val SELECTED_PLATFORM = stringPreferencesKey("selectedPlatform")
    }

    override fun getCurrentAppThemeSynchronously(): AppTheme = runBlocking {
        try {
            val appThemeIndex = settingsDataStore
                .data.first()[PreferencesKeys.CURRENT_APP_THEME] ?: 0

            val appThemeDto = AppThemeDto.entries.find {
                it.index == appThemeIndex
            } ?: AppThemeDto.DEFAULT

            appThemeDto.toAppTheme()
        } catch (e: Exception) {
            Timber.e("getCurrentAppThemeSynchronously Exception: $e")
            AppThemeDto.DEFAULT.toAppTheme()
        }
    }

    override fun isNotificationSoundActiveSynchronously(): Boolean = runBlocking {
        try {
            settingsDataStore.data.first()[PreferencesKeys.IS_NOTIFICATION_SOUND_ENABLED] ?: true
        } catch (e: Exception) {
            Timber.e("isNotificationSoundActiveSynchronously Exception: $e")
            true
        }
    }

    override fun isNotificationVibrateActiveSynchronously(): Boolean = runBlocking {
        try {
            settingsDataStore.data.first()[PreferencesKeys.IS_NOTIFICATION_VIBRATE_ENABLED] ?: true
        } catch (e: Exception) {
            Timber.e("isNotificationVibrateActiveSynchronously Exception: $e")
            true
        }
    }

    override suspend fun isOnBoardingCompleted(): Boolean = runBlocking {
        try {
            settingsDataStore.data.first()[PreferencesKeys.IS_ONBOARDING_COMPLETED] ?: false
        } catch (e: Exception) {
            Timber.e("isOnBoardingCompleted Exception: $e")
            false
        }
    }

    override suspend fun getNotificationPermissionCount(): Int = runBlocking {
        try {
            settingsDataStore.data.first()[PreferencesKeys.NOTIFICATION_PERMISSION_COUNT] ?: 0
        } catch (e: Exception) {
            Timber.e("getNotificationPermissionCount Exception: $e")
            0
        }
    }

    override suspend fun getCurrentAppTheme(): AppTheme = try {
        val appThemeIndex = settingsDataStore.data.first()[PreferencesKeys.CURRENT_APP_THEME] ?: 0

        val appThemeDto = AppThemeDto.entries.find {
            it.index == appThemeIndex
        } ?: AppThemeDto.DEFAULT

        appThemeDto.toAppTheme()
    } catch (e: Exception) {
        Timber.e("getCurrentAppTheme Exception: $e")
        AppThemeDto.DEFAULT.toAppTheme()
    }

    override suspend fun getCurrentAppLocale(): AppLocale = try {
        val appLocaleList = AppCompatDelegate.getApplicationLocales()

        if (appLocaleList.isEmpty) {
            Timber.i("App locale list is Empty.")
            AppLocaleDto.DEFAULT.toAppLocale()
        } else {
            val localeString = appLocaleList[0].toString()
            Timber.i("Current app locale string is $localeString")

            val appLocaleDto = AppLocaleDto.entries.find {
                it.localeString == localeString
            } ?: AppLocaleDto.DEFAULT

            appLocaleDto.toAppLocale()
        }
    } catch (e: Exception) {
        Timber.e("getCurrentAppLocaleString Exception: $e")
        AppLocaleDto.DEFAULT.toAppLocale()
    }

    override suspend fun isNotificationSoundEnabled(): Boolean = try {
        settingsDataStore.data.first()[PreferencesKeys.IS_NOTIFICATION_SOUND_ENABLED] ?: true
    } catch (e: Exception) {
        Timber.e("isNotificationSoundEnabled Exception: $e")
        true
    }

    override suspend fun isNotificationVibrateEnabled(): Boolean = try {
        settingsDataStore.data.first()[PreferencesKeys.IS_NOTIFICATION_VIBRATE_ENABLED] ?: true
    } catch (e: Exception) {
        Timber.e("isNotificationVibrateEnabled Exception: $e")
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

    override suspend fun getSelectedPlatform(): String = try {
        settingsDataStore.data
            .first()[PreferencesKeys.SELECTED_PLATFORM] ?: SELECTED_PLATFORM_CONSOLE
    } catch (e: Exception) {
        Timber.e("getSelectedPlatform Exception: $e")
        SELECTED_PLATFORM_CONSOLE
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

    override suspend fun setNotificationPermissionCount(count: Int) {
        try {
            settingsDataStore.edit { preferences ->
                preferences[PreferencesKeys.NOTIFICATION_PERMISSION_COUNT] = count
                Timber.i("Notification permission count edited to $count")
            }
        } catch (e: Exception) {
            Timber.e("setNotificationPermissionCount Exception: $e")
        }
    }

    override suspend fun setCurrentAppTheme(appThemeDto: AppThemeDto) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.CURRENT_APP_THEME] = appThemeDto.index
                Timber.i("AppTheme edited to ${appThemeDto.index}")
            }
        } catch (e: Exception) {
            Timber.e("setCurrentAppTheme Exception: $e")
        }
    }

    override suspend fun setCurrentAppLocale(
        appLocaleDto: AppLocaleDto
    ): IsActivityRestartNeeded = try {
        val isActivityRestartNeeded = isActivityRestartNeeded(appLocaleDto.direction)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(appLocaleDto.tag))
        isActivityRestartNeeded
    } catch (e: Exception) {
        Timber.e("setCurrentAppLocale Exception: $e")
        false
    }

    override suspend fun isNotificationSoundEnabled(isEnabled: Boolean) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.IS_NOTIFICATION_SOUND_ENABLED] = isEnabled
                Timber.i("isNotificationSoundEnabled edited to $isEnabled")
            }
        } catch (e: Exception) {
            Timber.e("isNotificationSoundEnabled Exception: $e")
        }
    }

    override suspend fun isNotificationVibrateEnabled(isEnabled: Boolean) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.IS_NOTIFICATION_VIBRATE_ENABLED] = isEnabled
                Timber.i("isNotificationVibrateEnabled edited to $isEnabled")
            }
        } catch (e: Exception) {
            Timber.e("isNotificationVibrateEnabled Exception: $e")
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

    override suspend fun setSelectedPlatform(platform: String) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.SELECTED_PLATFORM] = platform
                Timber.i("SelectedPlatform edited to $platform")
            }
        } catch (e: Exception) {
            Timber.e("setSelectedPlatform Exception: $e")
        }
    }

    private fun isActivityRestartNeeded(newLayoutDirection: Int): Boolean {
        val currentLocale = AppCompatDelegate.getApplicationLocales()[0]
        val currentLayoutDirection = currentLocale?.layoutDirection
        return currentLayoutDirection != newLayoutDirection
    }
}
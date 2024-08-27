package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto.AppLocaleDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto.AppThemeDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto.PlatformDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto.RateAppOptionDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.AppUpdateDialogShowCount
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.IsActivityRestartNeeded
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.IsAppUpdateDialogShownToday
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreviousRateAppRequestTimeInMs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.todayIn
import timber.log.Timber
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val settingsDataStore: DataStore<Preferences>
) : PreferencesRepository {

    private object PreferencesKeys {
        val CURRENT_APP_THEME = intPreferencesKey(name = "theme")
        val IS_ONBOARDING_COMPLETED = booleanPreferencesKey(name = "isOnBoardingCompleted")
        val NOTIFICATION_PERMISSION_COUNT = intPreferencesKey(name = "notificationPermissionCount")
        val IS_NOTIFICATION_SOUND_ENABLED = booleanPreferencesKey(
            name = "isNotificationSoundActive"
        )
        val IS_NOTIFICATION_VIBRATE_ENABLED = booleanPreferencesKey(
            name = "isNotificationVibrateActive"
        )
        val APP_UPDATE_DIALOG_SHOW_COUNT = intPreferencesKey(name = "AppUpdateDialogShowCount")
        val APP_UPDATE_DIALOG_SHOW_EPOCH_DAYS = intPreferencesKey(
            name = "appUpdateDialogShowEpochDays"
        )
        val SELECTED_RATE_APP_OPTION = stringPreferencesKey(name = "selectedRateAppOption")
        val PREVIOUS_RATE_APP_REQUEST_TIME_IN_MS = longPreferencesKey(
            name = "previousRateAppRequestTimeInMs"
        )
        val PARTNER_ID = stringPreferencesKey(name = "partnerId")
        val SECRET_KEY = stringPreferencesKey(name = "secretKey")
        val SELECTED_PLATFORM = stringPreferencesKey(name = "selectedPlatform")
    }

    override fun getCurrentAppThemeSynchronously(): AppTheme = runBlocking {
        try {
            val appThemeIndex = settingsDataStore
                .data.first()[PreferencesKeys.CURRENT_APP_THEME] ?: 0

            val appThemeDto = when (appThemeIndex) {
                AppThemeDto.Default.index -> AppThemeDto.Default
                AppThemeDto.Light.index -> AppThemeDto.Light
                AppThemeDto.Dark.index -> AppThemeDto.Dark
                else -> AppThemeDto.Default
            }

            appThemeDto.toAppTheme()
        } catch (e: Exception) {
            Timber.e("getCurrentAppThemeSynchronously failed:")
            e.printStackTrace()
            AppThemeDto.Default.toAppTheme()
        }
    }

    override fun getCurrentAppTheme(): Flow<AppTheme> = settingsDataStore.data.map {
        val appThemeIndex = it[PreferencesKeys.CURRENT_APP_THEME] ?: 0

        val appThemeDto = when (appThemeIndex) {
            AppThemeDto.Default.index -> AppThemeDto.Default
            AppThemeDto.Light.index -> AppThemeDto.Light
            AppThemeDto.Dark.index -> AppThemeDto.Dark
            else -> AppThemeDto.Default
        }

        appThemeDto.toAppTheme()
    }.catch { e ->
        Timber.e("getCurrentAppTheme failed:")
        e.printStackTrace()
    }

    override fun getCurrentAppLocale(): AppLocale = try {
        val appLocaleList = AppCompatDelegate.getApplicationLocales()

        if (appLocaleList.isEmpty) {
            Timber.i("App locale list is Empty.")
            AppLocaleDto.Default.toAppLocale()
        } else {
            val localeString = appLocaleList[0].toString()
            Timber.i("Current app locale string is $localeString")

            val appLocaleDto = when (localeString) {
                AppLocaleDto.Default.localeString -> AppLocaleDto.Default
                AppLocaleDto.EnglishUS.localeString -> AppLocaleDto.EnglishUS
                AppLocaleDto.EnglishGB.localeString -> AppLocaleDto.EnglishGB
                AppLocaleDto.FarsiIR.localeString -> AppLocaleDto.FarsiIR
                else -> AppLocaleDto.Default
            }

            appLocaleDto.toAppLocale()
        }
    } catch (e: Exception) {
        Timber.e("getCurrentAppLocale failed:")
        e.printStackTrace()
        AppLocaleDto.Default.toAppLocale()
    }

    override suspend fun isOnBoardingCompleted(): Boolean = try {
        settingsDataStore.data.first()[PreferencesKeys.IS_ONBOARDING_COMPLETED] ?: false
    } catch (e: Exception) {
        Timber.e("isOnBoardingCompleted failed:")
        e.printStackTrace()
        false
    }

    override suspend fun getNotificationPermissionCount(): Int = try {
        settingsDataStore.data.first()[PreferencesKeys.NOTIFICATION_PERMISSION_COUNT] ?: 0
    } catch (e: Exception) {
        Timber.e("getNotificationPermissionCount failed:")
        e.printStackTrace()
        0
    }

    override fun isNotificationSoundEnabled(): Flow<Boolean> = settingsDataStore.data.map {
        it[PreferencesKeys.IS_NOTIFICATION_SOUND_ENABLED] ?: true
    }.catch { e ->
        Timber.e("isNotificationSoundEnabled failed:")
        e.printStackTrace()
    }

    override fun isNotificationVibrateEnabled(): Flow<Boolean> = settingsDataStore.data.map {
        it[PreferencesKeys.IS_NOTIFICATION_VIBRATE_ENABLED] ?: true
    }.catch { e ->
        Timber.e("isNotificationVibrateEnabled failed:")
        e.printStackTrace()
    }

    override fun getAppUpdateDialogShowCount(): Flow<AppUpdateDialogShowCount> =
        settingsDataStore.data.map {
            it[PreferencesKeys.APP_UPDATE_DIALOG_SHOW_COUNT] ?: 0
        }.catch { e ->
            Timber.e("getAppUpdateDialogShowCount failed:")
            e.printStackTrace()
        }

    override fun isAppUpdateDialogShownToday(): Flow<IsAppUpdateDialogShownToday> =
        settingsDataStore.data.map {
            val dialogShowEpochDays = it[PreferencesKeys.APP_UPDATE_DIALOG_SHOW_EPOCH_DAYS]

            dialogShowEpochDays?.let { epochDays ->
                val todayDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
                val dialogShowLocalDate = LocalDate.fromEpochDays(epochDays)

                val isShownToday = dialogShowLocalDate.periodUntil(todayDate).days == 0

                isShownToday
            } ?: false
        }.catch { e ->
            Timber.e("isAppUpdateDialogShownToday failed:")
            e.printStackTrace()
        }

    override suspend fun getSelectedRateAppOption(): RateAppOption = try {
        val selectedRateAppOption = settingsDataStore.data
            .first()[PreferencesKeys.SELECTED_RATE_APP_OPTION]

        val rateAppOptionDto = RateAppOptionDto.entries.find {
            it.value == selectedRateAppOption
        } ?: RateAppOptionDto.NOT_SHOWN_YET

        rateAppOptionDto.toRateAppOption()
    } catch (e: Exception) {
        Timber.e("getSelectedRateAppOption failed:")
        e.printStackTrace()
        RateAppOptionDto.NOT_SHOWN_YET.toRateAppOption()
    }

    override suspend fun getPreviousRateAppRequestTimeInMs(): PreviousRateAppRequestTimeInMs? =
        try {
            settingsDataStore.data.first()[PreferencesKeys.PREVIOUS_RATE_APP_REQUEST_TIME_IN_MS]
        } catch (e: Exception) {
            Timber.e("getPreviousRateAppRequestTimeInMs failed:")
            e.printStackTrace()
            null
        }

    override suspend fun getPartnerId(): String? = try {
        settingsDataStore.data.first()[PreferencesKeys.PARTNER_ID]
    } catch (e: Exception) {
        Timber.e("getPartnerId failed:")
        e.printStackTrace()
        null
    }

    override suspend fun getSecretKey(): String? = try {
        settingsDataStore.data.first()[PreferencesKeys.SECRET_KEY]
    } catch (e: Exception) {
        Timber.e("getSecretKey failed:")
        e.printStackTrace()
        null
    }

    override fun getSelectedPlatform(): Flow<Platform> = settingsDataStore.data.map {
        val selectedPlatform = it[PreferencesKeys.SELECTED_PLATFORM]

        val platformDto = PlatformDto.entries.find { platformDto ->
            platformDto.value == selectedPlatform
        } ?: PlatformDto.CONSOLE

        platformDto.toPlatform()
    }.catch { e ->
        Timber.e("getSelectedPlatform failed:")
        e.printStackTrace()
    }

    override suspend fun setCurrentAppTheme(appThemeDto: AppThemeDto) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.CURRENT_APP_THEME] = appThemeDto.index
                Timber.i("AppTheme edited to ${appThemeDto.index}")
            }
        } catch (e: Exception) {
            Timber.e("setCurrentAppTheme failed:")
            e.printStackTrace()
        }
    }

    override suspend fun setCurrentAppLocale(
        newAppLocaleDto: AppLocaleDto
    ): IsActivityRestartNeeded = try {
        val isActivityRestartNeeded = isActivityRestartNeeded(newAppLocaleDto)

        AppCompatDelegate.setApplicationLocales(
            /* locales = */ LocaleListCompat.forLanguageTags(newAppLocaleDto.languageTag)
        )

        isActivityRestartNeeded
    } catch (e: Exception) {
        Timber.e("setCurrentAppLocale failed:")
        e.printStackTrace()
        false
    }

    override suspend fun isOnBoardingCompleted(isCompleted: Boolean) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.IS_ONBOARDING_COMPLETED] = isCompleted
                Timber.i("isOnBoardingCompleted edited to $isCompleted")
            }
        } catch (e: Exception) {
            Timber.e("isOnBoardingCompleted failed:")
            e.printStackTrace()
        }
    }

    override suspend fun setNotificationPermissionCount(count: Int) {
        try {
            settingsDataStore.edit { preferences ->
                preferences[PreferencesKeys.NOTIFICATION_PERMISSION_COUNT] = count
                Timber.i("Notification permission count edited to $count")
            }
        } catch (e: Exception) {
            Timber.e("setNotificationPermissionCount failed:")
            e.printStackTrace()
        }
    }

    override suspend fun isNotificationSoundEnabled(isEnabled: Boolean) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.IS_NOTIFICATION_SOUND_ENABLED] = isEnabled
                Timber.i("isNotificationSoundEnabled edited to $isEnabled")
            }
        } catch (e: Exception) {
            Timber.e("isNotificationSoundEnabled failed:")
            e.printStackTrace()
        }
    }

    override suspend fun isNotificationVibrateEnabled(isEnabled: Boolean) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.IS_NOTIFICATION_VIBRATE_ENABLED] = isEnabled
                Timber.i("isNotificationVibrateEnabled edited to $isEnabled")
            }
        } catch (e: Exception) {
            Timber.e("isNotificationVibrateEnabled failed:")
            e.printStackTrace()
        }
    }

    override suspend fun setAppUpdateDialogShowCount(count: Int) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.APP_UPDATE_DIALOG_SHOW_COUNT] = count
                Timber.i("App update dialog show count edited to $count")
            }
        } catch (e: Exception) {
            Timber.e("setAppUpdateDialogShowCount failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeAppUpdateDialogShowEpochDays() {
        try {
            val todayLocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val todayEpochDays = todayLocalDate.toEpochDays()

            settingsDataStore.edit {
                it[PreferencesKeys.APP_UPDATE_DIALOG_SHOW_EPOCH_DAYS] = todayEpochDays
                Timber.i("App update dialog show epoch days edited to $todayEpochDays")
            }
        } catch (e: Exception) {
            Timber.e("storeAppUpdateDialogShowEpochDays failed:")
            e.printStackTrace()
        }
    }

    override suspend fun removeAppUpdateDialogShowEpochDays() {
        try {
            settingsDataStore.edit {
                it.remove(PreferencesKeys.APP_UPDATE_DIALOG_SHOW_EPOCH_DAYS)
                Timber.i("App update dialog show epoch days removed")
            }
        } catch (e: Exception) {
            Timber.e("removeAppUpdateDialogShowEpochDays failed:")
            e.printStackTrace()
        }
    }

    override suspend fun setSelectedRateAppOption(rateAppOptionDto: RateAppOptionDto) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.SELECTED_RATE_APP_OPTION] = rateAppOptionDto.value
                Timber.i("setSelectedRateAppOption edited to ${rateAppOptionDto.value}")
            }
        } catch (e: Exception) {
            Timber.e("setSelectedRateAppOption failed:")
            e.printStackTrace()
        }
    }

    override suspend fun setPreviousRateAppRequestTimeInMs() {
        try {
            val currentTimeInMs = DateHelper.getCurrentTimeInMillis()
            settingsDataStore.edit {
                it[PreferencesKeys.PREVIOUS_RATE_APP_REQUEST_TIME_IN_MS] = currentTimeInMs
                Timber.i("setPreviousRateAppRequestTimeInMs edited to $currentTimeInMs")
            }
        } catch (e: Exception) {
            Timber.e("setPreviousRateAppRequestTimeInMs failed:")
            e.printStackTrace()
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
            Timber.e("setPartnerId failed:")
            e.printStackTrace()
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
            Timber.e("setSecretKey failed:")
            e.printStackTrace()
        }
    }

    override suspend fun setSelectedPlatform(platformDto: PlatformDto) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.SELECTED_PLATFORM] = platformDto.value
                Timber.i("SelectedPlatform edited to ${platformDto.value}")
            }
        } catch (e: Exception) {
            Timber.e("setSelectedPlatform failed:")
            e.printStackTrace()
        }
    }

    private fun isActivityRestartNeeded(
        newLocale: AppLocaleDto
    ): Boolean = getCurrentAppLocale().layoutDirectionCompose != newLocale.layoutDirectionCompose
}
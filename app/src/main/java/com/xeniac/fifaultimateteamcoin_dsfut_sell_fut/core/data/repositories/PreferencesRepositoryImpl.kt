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

            when (appThemeIndex) {
                AppTheme.Default.index -> AppTheme.Default
                AppTheme.Light.index -> AppTheme.Light
                AppTheme.Dark.index -> AppTheme.Dark
                else -> AppTheme.Default
            }
        } catch (e: Exception) {
            Timber.e("getCurrentAppThemeSynchronously failed:")
            e.printStackTrace()
            AppTheme.Default
        }
    }

    override fun getCurrentAppTheme(): Flow<AppTheme> = settingsDataStore.data.map {
        val appThemeIndex = it[PreferencesKeys.CURRENT_APP_THEME] ?: 0

        when (appThemeIndex) {
            AppTheme.Default.index -> AppTheme.Default
            AppTheme.Light.index -> AppTheme.Light
            AppTheme.Dark.index -> AppTheme.Dark
            else -> AppTheme.Default
        }
    }.catch { e ->
        Timber.e("getCurrentAppTheme failed:")
        e.printStackTrace()
    }

    override fun getCurrentAppLocale(): AppLocale = try {
        val appLocaleList = AppCompatDelegate.getApplicationLocales()

        if (appLocaleList.isEmpty) {
            Timber.i("App locale list is Empty.")
            AppLocale.Default
        } else {
            val localeString = appLocaleList[0].toString()
            Timber.i("Current app locale string is $localeString")

            when (localeString) {
                AppLocale.Default.localeString -> AppLocale.Default
                AppLocale.EnglishUS.localeString -> AppLocale.EnglishUS
                AppLocale.EnglishGB.localeString -> AppLocale.EnglishGB
                AppLocale.FarsiIR.localeString -> AppLocale.FarsiIR
                else -> AppLocale.Default
            }
        }
    } catch (e: Exception) {
        Timber.e("getCurrentAppLocale failed:")
        e.printStackTrace()
        AppLocale.Default
    }

    override suspend fun isOnBoardingCompleted(): Boolean = try {
        settingsDataStore.data.first()[PreferencesKeys.IS_ONBOARDING_COMPLETED] ?: false
    } catch (e: Exception) {
        Timber.e("isOnBoardingCompleted failed:")
        e.printStackTrace()
        false
    }

    override fun getNotificationPermissionCount(): Flow<Int> = settingsDataStore.data.map {
        it[PreferencesKeys.NOTIFICATION_PERMISSION_COUNT] ?: 0
    }.catch { e ->
        Timber.e("getNotificationPermissionCount failed:")
        e.printStackTrace()
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

    override fun getSelectedRateAppOption(): Flow<RateAppOption> =
        settingsDataStore.data.map {
            val selectedRateAppOption = it[PreferencesKeys.SELECTED_RATE_APP_OPTION]

            RateAppOption.entries.find { rateAppOption ->
                rateAppOption.value == selectedRateAppOption
            } ?: RateAppOption.NOT_SHOWN_YET
        }.catch { e ->
            Timber.e("getSelectedRateAppOption failed:")
            e.printStackTrace()
        }

    override fun getPreviousRateAppRequestTimeInMs(): Flow<PreviousRateAppRequestTimeInMs?> =
        settingsDataStore.data.map {
            it[PreferencesKeys.PREVIOUS_RATE_APP_REQUEST_TIME_IN_MS]
        }.catch { e ->
            Timber.e("getPreviousRateAppRequestTimeInMs failed:")
            e.printStackTrace()
        }

    override fun getPartnerId(): Flow<String?> = settingsDataStore.data.map {
        it[PreferencesKeys.PARTNER_ID]
    }.catch { e ->
        Timber.e("getPartnerId failed:")
        e.printStackTrace()
    }

    override fun getSecretKey(): Flow<String?> = settingsDataStore.data.map {
        it[PreferencesKeys.SECRET_KEY]
    }.catch { e ->
        Timber.e("getSecretKey failed:")
        e.printStackTrace()
    }

    override fun getSelectedPlatform(): Flow<Platform> = settingsDataStore.data.map {
        val selectedPlatform = it[PreferencesKeys.SELECTED_PLATFORM]

        Platform.entries.find { platform ->
            platform.value == selectedPlatform
        } ?: Platform.CONSOLE
    }.catch { e ->
        Timber.e("getSelectedPlatform failed:")
        e.printStackTrace()
    }

    override suspend fun storeCurrentAppTheme(appTheme: AppTheme) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.CURRENT_APP_THEME] = appTheme.index
                Timber.i("AppTheme edited to ${appTheme.index}")
            }
        } catch (e: Exception) {
            Timber.e("storeCurrentAppTheme failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeCurrentAppLocale(
        newAppLocale: AppLocale
    ): IsActivityRestartNeeded = try {
        val isActivityRestartNeeded = isActivityRestartNeeded(newAppLocale)

        AppCompatDelegate.setApplicationLocales(
            /* locales = */ LocaleListCompat.forLanguageTags(newAppLocale.languageTag)
        )

        isActivityRestartNeeded
    } catch (e: Exception) {
        Timber.e("storeCurrentAppLocale failed:")
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

    override suspend fun storeNotificationPermissionCount(count: Int) {
        try {
            settingsDataStore.edit { preferences ->
                preferences[PreferencesKeys.NOTIFICATION_PERMISSION_COUNT] = count
                Timber.i("Notification permission count edited to $count")
            }
        } catch (e: Exception) {
            Timber.e("storeNotificationPermissionCount failed:")
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

    override suspend fun storeAppUpdateDialogShowCount(count: Int) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.APP_UPDATE_DIALOG_SHOW_COUNT] = count
                Timber.i("App update dialog show count edited to $count")
            }
        } catch (e: Exception) {
            Timber.e("storeAppUpdateDialogShowCount failed:")
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

    override suspend fun storeSelectedRateAppOption(rateAppOption: RateAppOption) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.SELECTED_RATE_APP_OPTION] = rateAppOption.value
                Timber.i("setSelectedRateAppOption edited to ${rateAppOption.value}")
            }
        } catch (e: Exception) {
            Timber.e("storeSelectedRateAppOption failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storePreviousRateAppRequestTimeInMs() {
        try {
            val currentTimeInMs = DateHelper.getCurrentTimeInMillis()
            settingsDataStore.edit {
                it[PreferencesKeys.PREVIOUS_RATE_APP_REQUEST_TIME_IN_MS] = currentTimeInMs
                Timber.i("setPreviousRateAppRequestTimeInMs edited to $currentTimeInMs")
            }
        } catch (e: Exception) {
            Timber.e("storePreviousRateAppRequestTimeInMs failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storePartnerId(partnerId: String?) {
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
            Timber.e("storePartnerId failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeSecretKey(secretKey: String?) {
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
            Timber.e("storeSecretKey failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeSelectedPlatform(platform: Platform) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.SELECTED_PLATFORM] = platform.value
                Timber.i("SelectedPlatform edited to ${platform.value}")
            }
        } catch (e: Exception) {
            Timber.e("storeSelectedPlatform failed:")
            e.printStackTrace()
        }
    }

    private fun isActivityRestartNeeded(
        newLocale: AppLocale
    ): Boolean = getCurrentAppLocale().layoutDirectionCompose != newLocale.layoutDirectionCompose
}
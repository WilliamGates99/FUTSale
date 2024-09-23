package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.SettingsDataStoreQualifier
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.IsActivityRestartNeeded
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class SettingsDataStoreRepositoryImpl @Inject constructor(
    @SettingsDataStoreQualifier private val dataStore: DataStore<Preferences>
) : SettingsDataStoreRepository {

    private object PreferencesKeys {
        val IS_ONBOARDING_COMPLETED = booleanPreferencesKey(name = "isOnboardingCompleted")
        val NOTIFICATION_PERMISSION_COUNT = intPreferencesKey(name = "notificationPermissionCount")

        val CURRENT_APP_THEME = intPreferencesKey(name = "theme")
        val IS_NOTIFICATION_SOUND_ENABLED = booleanPreferencesKey(
            name = "isNotificationSoundActive"
        )
        val IS_NOTIFICATION_VIBRATE_ENABLED = booleanPreferencesKey(
            name = "isNotificationVibrateActive"
        )
    }

    override suspend fun isOnboardingCompleted(): Boolean = try {
        dataStore.data.first()[PreferencesKeys.IS_ONBOARDING_COMPLETED] ?: false
    } catch (e: Exception) {
        Timber.e("Get is onboarding completed failed:")
        e.printStackTrace()
        false
    }

    override fun getNotificationPermissionCount(): Flow<Int> = dataStore.data.map {
        it[PreferencesKeys.NOTIFICATION_PERMISSION_COUNT] ?: 0
    }.catch { e ->
        Timber.e("Get notification permission count failed:")
        e.printStackTrace()
    }

    override fun getCurrentAppThemeSynchronously(): AppTheme = try {
        val appThemeIndex = runBlocking {
            dataStore.data.first()[PreferencesKeys.CURRENT_APP_THEME] ?: 0
        }

        when (appThemeIndex) {
            AppTheme.Default.index -> AppTheme.Default
            AppTheme.Light.index -> AppTheme.Light
            AppTheme.Dark.index -> AppTheme.Dark
            else -> AppTheme.Default
        }
    } catch (e: Exception) {
        Timber.e("Get current app theme synchronously failed:")
        e.printStackTrace()
        AppTheme.Default
    }

    override fun getCurrentAppTheme(): Flow<AppTheme> = dataStore.data.map {
        val appThemeIndex = it[PreferencesKeys.CURRENT_APP_THEME] ?: 0

        when (appThemeIndex) {
            AppTheme.Default.index -> AppTheme.Default
            AppTheme.Light.index -> AppTheme.Light
            AppTheme.Dark.index -> AppTheme.Dark
            else -> AppTheme.Default
        }
    }.catch { e ->
        Timber.e("Get current app theme failed:")
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
        Timber.e("Get current app locale failed:")
        e.printStackTrace()
        AppLocale.Default
    }

    override fun isNotificationSoundEnabled(): Flow<Boolean> = dataStore.data.map {
        it[PreferencesKeys.IS_NOTIFICATION_SOUND_ENABLED] ?: true
    }.catch { e ->
        Timber.e("Get is notification sound enabled failed:")
        e.printStackTrace()
    }

    override fun isNotificationVibrateEnabled(): Flow<Boolean> = dataStore.data.map {
        it[PreferencesKeys.IS_NOTIFICATION_VIBRATE_ENABLED] ?: true
    }.catch { e ->
        Timber.e("Get is notification vibrate enabled failed:")
        e.printStackTrace()
    }

    override suspend fun isOnBoardingCompleted(isCompleted: Boolean) {
        try {
            dataStore.edit {
                it[PreferencesKeys.IS_ONBOARDING_COMPLETED] = isCompleted
                Timber.i("isOnBoardingCompleted edited to $isCompleted")
            }
        } catch (e: Exception) {
            Timber.e("Store is onboarding completed failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeNotificationPermissionCount(count: Int) {
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.NOTIFICATION_PERMISSION_COUNT] = count
                Timber.i("Notification permission count edited to $count")
            }
        } catch (e: Exception) {
            Timber.e("Store notification permission count failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeCurrentAppTheme(appTheme: AppTheme) {
        try {
            dataStore.edit {
                it[PreferencesKeys.CURRENT_APP_THEME] = appTheme.index
                Timber.i("Current app theme edited to ${appTheme.index}")
            }
        } catch (e: Exception) {
            Timber.e("Store current app theme failed:")
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
        Timber.e("Store current app locale failed:")
        e.printStackTrace()
        false
    }

    override suspend fun isNotificationSoundEnabled(isEnabled: Boolean) {
        try {
            dataStore.edit {
                it[PreferencesKeys.IS_NOTIFICATION_SOUND_ENABLED] = isEnabled
                Timber.i("Is notification sound enabled edited to $isEnabled")
            }
        } catch (e: Exception) {
            Timber.e("Store is notification sound enabled failed:")
            e.printStackTrace()
        }
    }

    override suspend fun isNotificationVibrateEnabled(isEnabled: Boolean) {
        try {
            dataStore.edit {
                it[PreferencesKeys.IS_NOTIFICATION_VIBRATE_ENABLED] = isEnabled
                Timber.i("Is notification vibrate enabled edited to $isEnabled")
            }
        } catch (e: Exception) {
            Timber.e("Store is notification vibrate enabled failed:")
            e.printStackTrace()
        }
    }

    private fun isActivityRestartNeeded(
        newLocale: AppLocale
    ): Boolean = getCurrentAppLocale().layoutDirectionCompose != newLocale.layoutDirectionCompose
}
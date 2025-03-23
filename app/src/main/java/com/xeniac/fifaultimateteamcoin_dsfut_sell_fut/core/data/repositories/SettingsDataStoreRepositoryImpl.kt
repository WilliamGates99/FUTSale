package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.SettingsDataStoreQualifier
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.SettingsPreferences
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
    @SettingsDataStoreQualifier private val dataStore: DataStore<SettingsPreferences>
) : SettingsDataStoreRepository {

    override suspend fun isOnboardingCompleted(): Boolean = try {
        dataStore.data.first().isOnboardingCompleted
    } catch (e: Exception) {
        Timber.e("Get is onboarding completed failed:")
        e.printStackTrace()
        false
    }

    override fun getNotificationPermissionCount(): Flow<Int> = dataStore.data.map {
        it.notificationPermissionCount
    }.catch { e ->
        Timber.e("Get notification permission count failed:")
        e.printStackTrace()
    }

    override fun getCurrentAppThemeSynchronously(): AppTheme = try {
        val appThemeIndex = runBlocking {
            dataStore.data.first().themeIndex
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
        val appThemeIndex = it.themeIndex

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
        it.isNotificationSoundEnabled
    }.catch { e ->
        Timber.e("Get is notification sound enabled failed:")
        e.printStackTrace()
    }

    override fun isNotificationVibrateEnabled(): Flow<Boolean> = dataStore.data.map {
        it.isNotificationVibrateEnabled
    }.catch { e ->
        Timber.e("Get is notification vibrate enabled failed:")
        e.printStackTrace()
    }

    override suspend fun isOnboardingCompleted(isCompleted: Boolean) {
        try {
            dataStore.updateData { it.copy(isOnboardingCompleted = isCompleted) }
            Timber.i("Is onboarding completed edited to $isCompleted")
        } catch (e: Exception) {
            Timber.e("Store is onboarding completed failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeNotificationPermissionCount(count: Int) {
        try {
            dataStore.updateData { it.copy(notificationPermissionCount = count) }
            Timber.i("Notification permission count edited to $count")
        } catch (e: Exception) {
            Timber.e("Store notification permission count failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeCurrentAppTheme(appTheme: AppTheme) {
        try {
            dataStore.updateData { it.copy(themeIndex = appTheme.index) }
            Timber.i("Current app theme edited to ${appTheme.index}")
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
            dataStore.updateData { it.copy(isNotificationSoundEnabled = isEnabled) }
            Timber.i("Is notification sound enabled edited to $isEnabled")
        } catch (e: Exception) {
            Timber.e("Store is notification sound enabled failed:")
            e.printStackTrace()
        }
    }

    override suspend fun isNotificationVibrateEnabled(isEnabled: Boolean) {
        try {
            dataStore.updateData { it.copy(isNotificationVibrateEnabled = isEnabled) }
            Timber.i("Is notification vibrate enabled edited to $isEnabled")
        } catch (e: Exception) {
            Timber.e("Store is notification vibrate enabled failed:")
            e.printStackTrace()
        }
    }

    private fun isActivityRestartNeeded(
        newLocale: AppLocale
    ): Boolean = getCurrentAppLocale().layoutDirectionCompose != newLocale.layoutDirectionCompose
}
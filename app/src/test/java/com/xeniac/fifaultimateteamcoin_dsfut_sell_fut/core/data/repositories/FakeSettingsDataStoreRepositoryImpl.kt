package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.IsActivityRestartNeeded
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeSettingsDataStoreRepositoryImpl : SettingsDataStoreRepository {

    var isOnBoardingCompleted = false
    var notificationPermissionCount = 0
    var currentAppTheme: AppTheme = AppTheme.Default
    var currentLocale: AppLocale = AppLocale.Default
    var isNotificationSoundEnabled = SnapshotStateList<Boolean>().apply { add(true) }
    var isNotificationVibrateEnabled = SnapshotStateList<Boolean>().apply { add(true) }

    override suspend fun isOnboardingCompleted(): Boolean = isOnBoardingCompleted

    override fun getNotificationPermissionCount(): Flow<Int> = flow {
        emit(notificationPermissionCount)
    }

    override fun getCurrentAppThemeSynchronously(): AppTheme = currentAppTheme

    override fun getCurrentAppTheme(): Flow<AppTheme> = flow { emit(currentAppTheme) }

    override fun getCurrentAppLocale(): AppLocale = currentLocale

    override fun isNotificationSoundEnabled(): Flow<Boolean> = snapshotFlow {
        isNotificationSoundEnabled.first()
    }

    override fun isNotificationVibrateEnabled(): Flow<Boolean> = snapshotFlow {
        isNotificationVibrateEnabled.first()
    }

    override suspend fun isOnboardingCompleted(isCompleted: Boolean) {
        isOnBoardingCompleted = isCompleted
    }

    override suspend fun storeNotificationPermissionCount(count: Int) {
        notificationPermissionCount = count
    }

    override suspend fun storeCurrentAppTheme(appTheme: AppTheme) {
        currentAppTheme = appTheme
    }

    override suspend fun storeCurrentAppLocale(
        newAppLocale: AppLocale
    ): IsActivityRestartNeeded {
        val isActivityRestartNeeded = isActivityRestartNeeded(newAppLocale)

        currentLocale = newAppLocale

        return isActivityRestartNeeded
    }

    override suspend fun isNotificationSoundEnabled(isEnabled: Boolean) {
        isNotificationSoundEnabled.apply {
            clear()
            add(isEnabled)
        }
    }

    override suspend fun isNotificationVibrateEnabled(isEnabled: Boolean) {
        isNotificationVibrateEnabled.apply {
            clear()
            add(isEnabled)
        }
    }

    fun isActivityRestartNeeded(
        newLocale: AppLocale
    ): Boolean = currentLocale.layoutDirectionCompose != newLocale.layoutDirectionCompose
}
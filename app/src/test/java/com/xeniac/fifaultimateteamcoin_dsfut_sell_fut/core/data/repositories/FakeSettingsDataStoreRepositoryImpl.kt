package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.IsActivityRestartNeeded
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.flow.Flow

class FakeSettingsDataStoreRepositoryImpl : SettingsDataStoreRepository {

    var currentAppTheme = SnapshotStateList<AppTheme>().apply { add(AppTheme.DEFAULT) }
    var currentLocale: AppLocale = AppLocale.DEFAULT
    var isNotificationSoundEnabled = SnapshotStateList<Boolean>().apply { add(true) }
    var isNotificationVibrateEnabled = SnapshotStateList<Boolean>().apply { add(true) }

    override fun getCurrentAppThemeSynchronously(): AppTheme = currentAppTheme.first()

    override fun getCurrentAppTheme(): Flow<AppTheme> = snapshotFlow {
        currentAppTheme.first()
    }

    override suspend fun storeCurrentAppTheme(appTheme: AppTheme) {
        currentAppTheme.apply {
            clear()
            add(appTheme)
        }
    }

    override fun getCurrentAppLocale(): AppLocale = currentLocale

    override suspend fun storeCurrentAppLocale(
        newAppLocale: AppLocale
    ): IsActivityRestartNeeded {
        val isActivityRestartNeeded = isActivityRestartNeeded(newAppLocale)

        currentLocale = newAppLocale

        return isActivityRestartNeeded
    }

    override fun isNotificationSoundEnabled(): Flow<Boolean> = snapshotFlow {
        isNotificationSoundEnabled.first()
    }

    override suspend fun isNotificationSoundEnabled(isEnabled: Boolean) {
        isNotificationSoundEnabled.apply {
            clear()
            add(isEnabled)
        }
    }

    override fun isNotificationVibrateEnabled(): Flow<Boolean> = snapshotFlow {
        isNotificationVibrateEnabled.first()
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
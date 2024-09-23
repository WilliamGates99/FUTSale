package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import kotlinx.coroutines.flow.Flow

typealias IsActivityRestartNeeded = Boolean

interface SettingsDataStoreRepository {

    suspend fun isOnboardingCompleted(): Boolean

    fun getNotificationPermissionCount(): Flow<Int>

    fun getCurrentAppThemeSynchronously(): AppTheme

    fun getCurrentAppTheme(): Flow<AppTheme>

    fun getCurrentAppLocale(): AppLocale

    fun isNotificationSoundEnabled(): Flow<Boolean>

    fun isNotificationVibrateEnabled(): Flow<Boolean>

    suspend fun isOnboardingCompleted(isCompleted: Boolean)

    suspend fun storeNotificationPermissionCount(count: Int)

    suspend fun storeCurrentAppTheme(appTheme: AppTheme)

    suspend fun storeCurrentAppLocale(newAppLocale: AppLocale): IsActivityRestartNeeded

    suspend fun isNotificationSoundEnabled(isEnabled: Boolean)

    suspend fun isNotificationVibrateEnabled(isEnabled: Boolean)
}
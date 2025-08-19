package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import kotlinx.coroutines.flow.Flow

typealias IsActivityRestartNeeded = Boolean

interface SettingsDataStoreRepository {

    fun getCurrentAppThemeSynchronously(): AppTheme

    fun getCurrentAppTheme(): Flow<AppTheme>

    suspend fun storeCurrentAppTheme(appTheme: AppTheme)

    fun getCurrentAppLocale(): AppLocale

    suspend fun storeCurrentAppLocale(newAppLocale: AppLocale): IsActivityRestartNeeded

    fun isNotificationSoundEnabled(): Flow<Boolean>

    suspend fun isNotificationSoundEnabled(isEnabled: Boolean)

    fun isNotificationVibrateEnabled(): Flow<Boolean>

    suspend fun isNotificationVibrateEnabled(isEnabled: Boolean)
}
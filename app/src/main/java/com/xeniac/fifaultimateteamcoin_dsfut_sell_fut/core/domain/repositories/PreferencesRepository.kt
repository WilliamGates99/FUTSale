package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import kotlinx.coroutines.flow.Flow

typealias IsActivityRestartNeeded = Boolean
typealias AppUpdateDialogShowCount = Int
typealias IsAppUpdateDialogShownToday = Boolean
typealias PreviousRateAppRequestTimeInMs = Long

interface PreferencesRepository {

    fun getCurrentAppThemeSynchronously(): AppTheme

    fun getCurrentAppTheme(): Flow<AppTheme>

    fun getCurrentAppLocale(): AppLocale

    suspend fun isOnBoardingCompleted(): Boolean

    fun getNotificationPermissionCount(): Flow<Int>

    fun isNotificationSoundEnabled(): Flow<Boolean>

    fun isNotificationVibrateEnabled(): Flow<Boolean>

    fun getAppUpdateDialogShowCount(): Flow<AppUpdateDialogShowCount>

    fun isAppUpdateDialogShownToday(): Flow<IsAppUpdateDialogShownToday>

    fun getSelectedRateAppOption(): Flow<RateAppOption>

    fun getPreviousRateAppRequestTimeInMs(): Flow<PreviousRateAppRequestTimeInMs?>

    fun getPartnerId(): Flow<String?>

    fun getSecretKey(): Flow<String?>

    fun getSelectedPlatform(): Flow<Platform>

    suspend fun storeCurrentAppTheme(appTheme: AppTheme)

    suspend fun storeCurrentAppLocale(newAppLocale: AppLocale): IsActivityRestartNeeded

    suspend fun isOnBoardingCompleted(isCompleted: Boolean)

    suspend fun storeNotificationPermissionCount(count: Int)

    suspend fun isNotificationSoundEnabled(isEnabled: Boolean)

    suspend fun isNotificationVibrateEnabled(isEnabled: Boolean)

    suspend fun storeAppUpdateDialogShowCount(count: Int)

    suspend fun storeAppUpdateDialogShowEpochDays()

    suspend fun removeAppUpdateDialogShowEpochDays()

    suspend fun storeSelectedRateAppOption(rateAppOption: RateAppOption)

    suspend fun storePreviousRateAppRequestTimeInMs()

    suspend fun storePartnerId(partnerId: String?)

    suspend fun storeSecretKey(secretKey: String?)

    suspend fun storeSelectedPlatform(platform: Platform)
}
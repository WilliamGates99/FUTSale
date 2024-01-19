package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppTheme


typealias AppLocaleString = String

interface PreferencesRepository {

    fun getCurrentAppThemeIndexSynchronously(): AppTheme

    fun isNotificationSoundActiveSynchronously(): Boolean

    fun isNotificationVibrateActiveSynchronously(): Boolean

    suspend fun isOnBoardingCompleted(): Boolean

    suspend fun getNotificationPermissionCount(): Int

    suspend fun getCurrentAppThemeIndex(): AppTheme

    suspend fun getCurrentAppLocaleString(): AppLocaleString?

    suspend fun isNotificationSoundActive(): Boolean

    suspend fun isNotificationVibrateActive(): Boolean

    suspend fun getRateAppDialogChoice(): Int

    suspend fun getPreviousRequestTimeInMillis(): Long

    suspend fun getPartnerId(): String?

    suspend fun getSecretKey(): String?

    suspend fun getSelectedPlatform(): String

    suspend fun isOnBoardingCompleted(isCompleted: Boolean)

    suspend fun setNotificationPermissionCount(count: Int)

    suspend fun setCurrentAppTheme(index: Int)

    suspend fun setCurrentAppLocale(localeTag: String, newLayoutDirection: Int): Boolean

    suspend fun isNotificationSoundActive(isActive: Boolean)

    suspend fun isNotificationVibrateActive(isActive: Boolean)

    suspend fun setRateAppDialogChoice(value: Int)

    suspend fun setPreviousRequestTimeInMillis(timeInMillis: Long)

    suspend fun setPartnerId(partnerId: String?)

    suspend fun setSecretKey(secretKey: String?)

    suspend fun setSelectedPlatform(platform: String)
}
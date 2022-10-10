package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository

interface PreferencesRepository {

    fun getCurrentAppThemeSynchronously(): Int

    suspend fun isOnBoardingCompleted(): Boolean

    suspend fun getCurrentAppTheme(): Int

    suspend fun isNotificationSoundActive(): Boolean

    suspend fun isNotificationVibrateActive(): Boolean

    suspend fun getRateAppDialogChoice(): Int

    suspend fun getPreviousRequestTimeInMillis(): Long

    suspend fun getPartnerId(): String?

    suspend fun getSecretKey(): String?

    suspend fun isOnBoardingCompleted(isCompleted: Boolean)

    suspend fun setCurrentAppTheme(index: Int)

    suspend fun isNotificationSoundActive(isActive: Boolean)

    suspend fun isNotificationVibrateActive(isActive: Boolean)

    suspend fun setRateAppDialogChoice(value: Int)

    suspend fun setPreviousRequestTimeInMillis(timeInMillis: Long)

    suspend fun setPartnerId(partnerId: String)

    suspend fun setSecretKey(secretKey: String)
}
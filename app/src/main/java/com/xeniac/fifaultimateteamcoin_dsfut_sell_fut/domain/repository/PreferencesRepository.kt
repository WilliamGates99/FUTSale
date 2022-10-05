package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository

interface PreferencesRepository {

    suspend fun isUserLoggedIn(): Boolean

    suspend fun getCurrentAppTheme(): Int

    suspend fun getRateAppDialogChoice(): Int

    suspend fun getPreviousRequestTimeInMillis(): Long

    suspend fun isUserLoggedIn(value: Boolean)

    suspend fun setCurrentAppTheme(index: Int)

    suspend fun setRateAppDialogChoice(value: Int)

    suspend fun setPreviousRequestTimeInMillis(timeInMillis: Long)
}
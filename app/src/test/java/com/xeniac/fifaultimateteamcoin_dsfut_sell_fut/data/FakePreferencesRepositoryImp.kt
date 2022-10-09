package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository

class FakePreferencesRepositoryImp : PreferencesRepository {

    private var isOnBoardingCompleted = false
    private var currentAppTheme = 0
    private var rateAppDialogChoice = 0
    private var previousRequestTimeInMillis = 0L
    private var storedPartnerId: String? = null
    private var storedSecretKey: String? = null

    override fun getCurrentAppThemeSynchronously(): Int = currentAppTheme

    override suspend fun isOnBoardingCompleted(): Boolean = isOnBoardingCompleted

    override suspend fun getCurrentAppTheme(): Int = currentAppTheme

    override suspend fun getRateAppDialogChoice(): Int = rateAppDialogChoice

    override suspend fun getPreviousRequestTimeInMillis(): Long = previousRequestTimeInMillis

    override suspend fun getPartnerId(): String? = storedPartnerId

    override suspend fun getSecretKey(): String? = storedSecretKey

    override suspend fun isOnBoardingCompleted(value: Boolean) {
        isOnBoardingCompleted = value
    }

    override suspend fun setCurrentAppTheme(index: Int) {
        currentAppTheme = index
    }

    override suspend fun setRateAppDialogChoice(value: Int) {
        rateAppDialogChoice = value
    }

    override suspend fun setPreviousRequestTimeInMillis(timeInMillis: Long) {
        previousRequestTimeInMillis = timeInMillis
    }

    override suspend fun setPartnerId(partnerId: String) {
        storedPartnerId = partnerId
    }

    override suspend fun setSecretKey(secretKey: String) {
        storedSecretKey = secretKey
    }
}
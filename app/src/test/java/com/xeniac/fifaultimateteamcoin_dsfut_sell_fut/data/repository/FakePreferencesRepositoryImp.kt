package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.SELECTED_PLATFORM_CONSOLE

class FakePreferencesRepositoryImp : PreferencesRepository {

    private var isOnBoardingCompleted = false
    private var currentAppTheme = 0
    private var isNotificationSoundActive = true
    private var isNotificationVibrateActive = true
    private var rateAppDialogChoice = 0
    private var previousRequestTimeInMillis = 0L
    private var storedPartnerId: String? = null
    private var storedSecretKey: String? = null
    private var selectedPlatform = SELECTED_PLATFORM_CONSOLE

    override fun getCurrentAppThemeSynchronously(): Int = currentAppTheme

    override fun isOnBoardingCompletedSynchronously(): Boolean = isOnBoardingCompleted

    override fun isNotificationSoundActiveSynchronously(): Boolean = isNotificationSoundActive

    override fun isNotificationVibrateActiveSynchronously(): Boolean = isNotificationVibrateActive

    override suspend fun getCurrentAppTheme(): Int = currentAppTheme

    override suspend fun isNotificationSoundActive(): Boolean = isNotificationSoundActive

    override suspend fun isNotificationVibrateActive(): Boolean = isNotificationVibrateActive

    override suspend fun getRateAppDialogChoice(): Int = rateAppDialogChoice

    override suspend fun getPreviousRequestTimeInMillis(): Long = previousRequestTimeInMillis

    override suspend fun getPartnerId(): String? = storedPartnerId

    override suspend fun getSecretKey(): String? = storedSecretKey

    override suspend fun getSelectedPlatform(): String = selectedPlatform

    override suspend fun isOnBoardingCompleted(isCompleted: Boolean) {
        isOnBoardingCompleted = isCompleted
    }

    override suspend fun setCurrentAppTheme(index: Int) {
        currentAppTheme = index
    }

    override suspend fun isNotificationSoundActive(isActive: Boolean) {
        isNotificationSoundActive = isActive
    }

    override suspend fun isNotificationVibrateActive(isActive: Boolean) {
        isNotificationVibrateActive = isActive
    }

    override suspend fun setRateAppDialogChoice(value: Int) {
        rateAppDialogChoice = value
    }

    override suspend fun setPreviousRequestTimeInMillis(timeInMillis: Long) {
        previousRequestTimeInMillis = timeInMillis
    }

    override suspend fun setPartnerId(partnerId: String?) {
        storedPartnerId = partnerId
    }

    override suspend fun setSecretKey(secretKey: String?) {
        storedSecretKey = secretKey
    }

    override suspend fun setSelectedPlatform(platform: String) {
        selectedPlatform = platform
    }
}
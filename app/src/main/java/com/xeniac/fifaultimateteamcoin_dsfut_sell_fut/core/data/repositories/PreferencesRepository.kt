package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.AppLocaleDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.AppThemeDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme

typealias IsActivityRestartNeeded = Boolean
typealias SelectedPlatform = String

interface PreferencesRepository {

    fun getCurrentAppThemeSynchronously(): AppTheme

    fun isNotificationSoundActiveSynchronously(): Boolean

    fun isNotificationVibrateActiveSynchronously(): Boolean

    suspend fun getCurrentAppTheme(): AppTheme

    suspend fun getCurrentAppLocale(): AppLocale

    suspend fun isOnBoardingCompleted(): Boolean

    suspend fun getNotificationPermissionCount(): Int

    suspend fun isNotificationSoundEnabled(): Boolean

    suspend fun isNotificationVibrateEnabled(): Boolean

    suspend fun getRateAppDialogChoice(): Int

    suspend fun getPreviousRequestTimeInMillis(): Long

    suspend fun getPartnerId(): String?

    suspend fun getSecretKey(): String?

    suspend fun getSelectedPlatform(): SelectedPlatform

    suspend fun setCurrentAppTheme(appThemeDto: AppThemeDto)

    suspend fun setCurrentAppLocale(appLocaleDto: AppLocaleDto): IsActivityRestartNeeded

    suspend fun isOnBoardingCompleted(isCompleted: Boolean)

    suspend fun setNotificationPermissionCount(count: Int)

    suspend fun isNotificationSoundEnabled(isEnabled: Boolean)

    suspend fun isNotificationVibrateEnabled(isEnabled: Boolean)

    suspend fun setRateAppDialogChoice(value: Int)

    suspend fun setPreviousRequestTimeInMillis(timeInMillis: Long)

    suspend fun setPartnerId(partnerId: String?)

    suspend fun setSecretKey(secretKey: String?)

    suspend fun setSelectedPlatform(platform: String)
}
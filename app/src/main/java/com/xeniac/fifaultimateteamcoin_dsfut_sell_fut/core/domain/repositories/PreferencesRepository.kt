package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.AppLocaleDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.AppThemeDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.PlatformDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.RateAppOptionDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import kotlinx.coroutines.flow.Flow

typealias isOnBoardingCompleted = Boolean
typealias IsActivityRestartNeeded = Boolean
typealias PreviousRateAppRequestTimeInMs = Long

interface PreferencesRepository {

    fun getCurrentAppThemeSynchronously(): AppTheme

    suspend fun getCurrentAppTheme(): AppTheme

    suspend fun getCurrentAppLocale(): AppLocale

    suspend fun isOnBoardingCompleted(): isOnBoardingCompleted

    suspend fun getNotificationPermissionCount(): Int

    suspend fun isNotificationSoundEnabled(): Boolean

    suspend fun isNotificationVibrateEnabled(): Boolean

    suspend fun getSelectedRateAppOption(): RateAppOption

    suspend fun getPreviousRateAppRequestTimeInMs(): PreviousRateAppRequestTimeInMs?

    suspend fun getPartnerId(): String?

    suspend fun getSecretKey(): String?

    suspend fun getSelectedPlatform(): Platform

    suspend fun setCurrentAppTheme(appThemeDto: AppThemeDto)

    suspend fun setCurrentAppLocale(appLocaleDto: AppLocaleDto): IsActivityRestartNeeded

    suspend fun isOnBoardingCompleted(isCompleted: Boolean)

    suspend fun setNotificationPermissionCount(count: Int)

    suspend fun isNotificationSoundEnabled(isEnabled: Boolean)

    suspend fun isNotificationVibrateEnabled(isEnabled: Boolean)

    suspend fun setSelectedRateAppOption(rateAppOptionDto: RateAppOptionDto)

    suspend fun setPreviousRateAppRequestTimeInMs()

    suspend fun setPartnerId(partnerId: String?)

    suspend fun setSecretKey(secretKey: String?)

    suspend fun setSelectedPlatform(platformDto: PlatformDto)
}
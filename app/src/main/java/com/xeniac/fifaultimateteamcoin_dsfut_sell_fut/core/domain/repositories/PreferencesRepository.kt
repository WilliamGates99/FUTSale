package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto.AppLocaleDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto.AppThemeDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto.PlatformDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto.RateAppOptionDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import kotlinx.coroutines.flow.Flow

typealias IsActivityRestartNeeded = Boolean
typealias PreviousRateAppRequestTimeInMs = Long

interface PreferencesRepository {

    fun getCurrentAppThemeSynchronously(): AppTheme

    fun getCurrentAppTheme(): Flow<AppTheme>

    fun getCurrentAppLocale(): AppLocale

    suspend fun isOnBoardingCompleted(): Boolean

    suspend fun getNotificationPermissionCount(): Int

    fun isNotificationSoundEnabled(): Flow<Boolean>

    fun isNotificationVibrateEnabled(): Flow<Boolean>

    suspend fun getSelectedRateAppOption(): RateAppOption

    suspend fun getPreviousRateAppRequestTimeInMs(): PreviousRateAppRequestTimeInMs?

    suspend fun getPartnerId(): String?

    suspend fun getSecretKey(): String?

    fun getSelectedPlatform(): Flow<Platform>

    suspend fun setCurrentAppTheme(appThemeDto: AppThemeDto)

    suspend fun setCurrentAppLocale(newAppLocaleDto: AppLocaleDto): IsActivityRestartNeeded

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
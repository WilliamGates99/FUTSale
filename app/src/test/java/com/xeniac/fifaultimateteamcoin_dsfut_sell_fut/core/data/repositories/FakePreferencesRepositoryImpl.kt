package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.AppLocaleDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.AppThemeDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.PlatformDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.RateAppOptionDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toAppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toAppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toPlatform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toRateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.IsActivityRestartNeeded
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreviousRateAppRequestTimeInMs

class FakePreferencesRepositoryImpl : PreferencesRepository {

    private var currentAppTheme: AppTheme = AppTheme.Default
    private var currentAppLocale: AppLocale = AppLocale.Default
    private var isOnBoardingCompleted = false
    private var notificationPermissionCount = 0
    private var isNotificationSoundEnabled = true
    private var isNotificationVibrateEnabled = true
    private var selectedRateAppOption: RateAppOption = RateAppOption.NOT_SHOWN_YET
    private var previousRateAppRequestTime: PreviousRateAppRequestTimeInMs? = null
    private var storedPartnerId: String? = null
    private var storedSecretKey: String? = null
    private var selectedPlatform: Platform = Platform.CONSOLE

    override fun getCurrentAppThemeSynchronously(): AppTheme = currentAppTheme

    override suspend fun getCurrentAppTheme(): AppTheme = currentAppTheme

    override suspend fun getCurrentAppLocale(): AppLocale = currentAppLocale

    override suspend fun isOnBoardingCompleted(): Boolean = isOnBoardingCompleted

    override suspend fun isOnBoardingCompleted(isCompleted: Boolean) {
        isOnBoardingCompleted = isCompleted
    }

    override suspend fun getNotificationPermissionCount(): Int = notificationPermissionCount

    override suspend fun isNotificationSoundEnabled(): Boolean = isNotificationSoundEnabled

    override suspend fun isNotificationSoundEnabled(isEnabled: Boolean) {
        isNotificationSoundEnabled = isEnabled
    }

    override suspend fun isNotificationVibrateEnabled(): Boolean = isNotificationVibrateEnabled

    override suspend fun isNotificationVibrateEnabled(isEnabled: Boolean) {
        isNotificationVibrateEnabled = isEnabled
    }

    override suspend fun getSelectedRateAppOption(): RateAppOption = selectedRateAppOption

    override suspend fun getPreviousRateAppRequestTimeInMs(): PreviousRateAppRequestTimeInMs? =
        previousRateAppRequestTime

    override suspend fun getPartnerId(): String? = storedPartnerId

    override suspend fun getSecretKey(): String? = storedSecretKey

    override suspend fun getSelectedPlatform(): Platform = selectedPlatform

    override suspend fun setCurrentAppTheme(appThemeDto: AppThemeDto) {
        currentAppTheme = appThemeDto.toAppTheme()
    }

    override suspend fun setCurrentAppLocale(appLocaleDto: AppLocaleDto): IsActivityRestartNeeded {
        currentAppLocale = appLocaleDto.toAppLocale()
        return false
    }

    override suspend fun setNotificationPermissionCount(count: Int) {
        notificationPermissionCount = count
    }

    override suspend fun setSelectedRateAppOption(rateAppOptionDto: RateAppOptionDto) {
        selectedRateAppOption = rateAppOptionDto.toRateAppOption()
    }

    override suspend fun setPreviousRateAppRequestTimeInMs() {
        previousRateAppRequestTime = DateHelper.getCurrentTimeInMillis()
    }

    override suspend fun setPartnerId(partnerId: String?) {
        storedPartnerId = partnerId
    }

    override suspend fun setSecretKey(secretKey: String?) {
        storedSecretKey = secretKey
    }

    override suspend fun setSelectedPlatform(platformDto: PlatformDto) {
        selectedPlatform = platformDto.toPlatform()
    }
}
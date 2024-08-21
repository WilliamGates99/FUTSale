package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakePreferencesRepositoryImpl @Inject constructor() : PreferencesRepository {

    var currentAppTheme: AppTheme = AppTheme.Default
    var currentLocale: AppLocale = AppLocale.Default
    var isOnBoardingCompleted = false
    var notificationPermissionCount = 0
    var isNotificationSoundEnabled = SnapshotStateList<Boolean>().apply {
        add(true)
    }
    var isNotificationVibrateEnabled = SnapshotStateList<Boolean>().apply {
        add(true)
    }
    var selectedRateAppOption: RateAppOption = RateAppOption.NOT_SHOWN_YET
    var previousRateAppRequestTime: PreviousRateAppRequestTimeInMs? = null
    var storedPartnerId: String? = null
    var storedSecretKey: String? = null
    var selectedPlatform = SnapshotStateList<Platform>().apply {
        add(Platform.CONSOLE)
    }

    fun changePartnerId(newPartnerId: String?) {
        storedPartnerId = newPartnerId
    }

    fun changeSecretKey(newSecretKey: String?) {
        storedSecretKey = newSecretKey
    }

    override fun getCurrentAppThemeSynchronously(): AppTheme = currentAppTheme

    override fun getCurrentAppTheme(): Flow<AppTheme> = flow { emit(currentAppTheme) }

    override fun getCurrentAppLocale(): AppLocale = currentLocale

    override suspend fun isOnBoardingCompleted(): Boolean = isOnBoardingCompleted

    override suspend fun isOnBoardingCompleted(isCompleted: Boolean) {
        isOnBoardingCompleted = isCompleted
    }

    override suspend fun getNotificationPermissionCount(): Int = notificationPermissionCount

    override fun isNotificationSoundEnabled(): Flow<Boolean> = snapshotFlow {
        isNotificationSoundEnabled.first()
    }

    override suspend fun isNotificationSoundEnabled(isEnabled: Boolean) {
        isNotificationSoundEnabled.apply {
            clear()
            add(isEnabled)
        }
    }

    override fun isNotificationVibrateEnabled(): Flow<Boolean> = snapshotFlow {
        isNotificationVibrateEnabled.first()
    }

    override suspend fun isNotificationVibrateEnabled(isEnabled: Boolean) {
        isNotificationVibrateEnabled.apply {
            clear()
            add(isEnabled)
        }
    }

    override suspend fun getSelectedRateAppOption(): RateAppOption = selectedRateAppOption

    override suspend fun getPreviousRateAppRequestTimeInMs(): PreviousRateAppRequestTimeInMs? =
        previousRateAppRequestTime

    override suspend fun getPartnerId(): String? = storedPartnerId

    override suspend fun getSecretKey(): String? = storedSecretKey

    override fun getSelectedPlatform(): Flow<Platform> = snapshotFlow { selectedPlatform.first() }

    override suspend fun setCurrentAppTheme(appThemeDto: AppThemeDto) {
        currentAppTheme = appThemeDto.toAppTheme()
    }

    override suspend fun setCurrentAppLocale(
        newAppLocaleDto: AppLocaleDto
    ): IsActivityRestartNeeded {
        val isActivityRestartNeeded = isActivityRestartNeeded(newAppLocaleDto)

        currentLocale = newAppLocaleDto.toAppLocale()

        return isActivityRestartNeeded
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
        selectedPlatform.apply {
            clear()
            add(platformDto.toPlatform())
        }
    }

    fun isActivityRestartNeeded(
        newLocale: AppLocaleDto
    ): Boolean = currentLocale.layoutDirectionCompose != newLocale.layoutDirectionCompose
}
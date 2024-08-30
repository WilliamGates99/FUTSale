package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.AppUpdateDialogShowCount
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.IsActivityRestartNeeded
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.IsAppUpdateDialogShownToday
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreviousRateAppRequestTimeInMs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.periodUntil
import kotlinx.datetime.todayIn

class FakePreferencesRepositoryImpl : PreferencesRepository {

    var currentAppTheme: AppTheme = AppTheme.Default
    var currentLocale: AppLocale = AppLocale.Default
    var isOnBoardingCompleted = false
    var notificationPermissionCount = 0
    var isNotificationSoundEnabled = SnapshotStateList<Boolean>().apply { add(true) }
    var isNotificationVibrateEnabled = SnapshotStateList<Boolean>().apply { add(true) }
    var appUpdateDialogShowCount = 0
    var appUpdateDialogShowEpochDays: Int? = null
    var selectedRateAppOption = RateAppOption.NOT_SHOWN_YET
    var previousRateAppRequestTime: PreviousRateAppRequestTimeInMs? = null
    var storedPartnerId: String? = null
    var storedSecretKey: String? = null
    var selectedPlatform = SnapshotStateList<Platform>().apply { add(Platform.CONSOLE) }

    private var shouldStoreTodayDate = true

    fun setShouldStoreTodayDate(value: Boolean) {
        shouldStoreTodayDate = value
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

    override fun getNotificationPermissionCount(): Flow<Int> = flow {
        emit(notificationPermissionCount)
    }

    override fun isNotificationSoundEnabled(): Flow<Boolean> = snapshotFlow {
        isNotificationSoundEnabled.first()
    }

    override fun isNotificationVibrateEnabled(): Flow<Boolean> = snapshotFlow {
        isNotificationVibrateEnabled.first()
    }

    override fun getAppUpdateDialogShowCount(): Flow<AppUpdateDialogShowCount> = flow {
        emit(appUpdateDialogShowCount)
    }

    override fun isAppUpdateDialogShownToday(): Flow<IsAppUpdateDialogShownToday> = snapshotFlow {
        val dialogShowEpochDays = appUpdateDialogShowEpochDays

        dialogShowEpochDays?.let { epochDays ->
            val todayDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val dialogShowLocalDate = LocalDate.fromEpochDays(epochDays)

            val isShownToday = dialogShowLocalDate.periodUntil(todayDate).days == 0

            isShownToday
        } ?: false
    }

    override fun getSelectedRateAppOption(): Flow<RateAppOption> = flow {
        emit(selectedRateAppOption)
    }

    override fun getPreviousRateAppRequestTimeInMs(): Flow<PreviousRateAppRequestTimeInMs?> = flow {
        emit(previousRateAppRequestTime)
    }

    override fun getPartnerId(): Flow<String?> = flow {
        emit(storedPartnerId)
    }

    override fun getSecretKey(): Flow<String?> = flow {
        emit(storedSecretKey)
    }

    override fun getSelectedPlatform(): Flow<Platform> = snapshotFlow { selectedPlatform.first() }

    override suspend fun storeCurrentAppTheme(appTheme: AppTheme) {
        currentAppTheme = appTheme
    }

    override suspend fun storeCurrentAppLocale(
        newAppLocale: AppLocale
    ): IsActivityRestartNeeded {
        val isActivityRestartNeeded = isActivityRestartNeeded(newAppLocale)

        currentLocale = newAppLocale

        return isActivityRestartNeeded
    }

    override suspend fun isOnBoardingCompleted(isCompleted: Boolean) {
        isOnBoardingCompleted = isCompleted
    }

    override suspend fun storeNotificationPermissionCount(count: Int) {
        notificationPermissionCount = count
    }

    override suspend fun isNotificationSoundEnabled(isEnabled: Boolean) {
        isNotificationSoundEnabled.apply {
            clear()
            add(isEnabled)
        }
    }

    override suspend fun isNotificationVibrateEnabled(isEnabled: Boolean) {
        isNotificationVibrateEnabled.apply {
            clear()
            add(isEnabled)
        }
    }

    override suspend fun storeAppUpdateDialogShowCount(count: Int) {
        appUpdateDialogShowCount = count
    }

    override suspend fun storeAppUpdateDialogShowEpochDays() {
        val todayLocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

        val localDate = if (shouldStoreTodayDate) {
            todayLocalDate
        } else {
            todayLocalDate.minus(
                value = 1,
                unit = DateTimeUnit.DAY
            )
        }

        appUpdateDialogShowEpochDays = localDate.toEpochDays()
    }

    override suspend fun removeAppUpdateDialogShowEpochDays() {
        appUpdateDialogShowEpochDays = null
    }

    override suspend fun storeSelectedRateAppOption(rateAppOption: RateAppOption) {
        selectedRateAppOption = rateAppOption
    }

    override suspend fun storePreviousRateAppRequestTimeInMs() {
        previousRateAppRequestTime = DateHelper.getCurrentTimeInMillis()
    }

    override suspend fun storePartnerId(partnerId: String?) {
        storedPartnerId = partnerId
    }

    override suspend fun storeSecretKey(secretKey: String?) {
        storedSecretKey = secretKey
    }

    override suspend fun storeSelectedPlatform(platform: Platform) {
        selectedPlatform.apply {
            clear()
            add(platform)
        }
    }

    fun isActivityRestartNeeded(
        newLocale: AppLocale
    ): Boolean = currentLocale.layoutDirectionCompose != newLocale.layoutDirectionCompose
}
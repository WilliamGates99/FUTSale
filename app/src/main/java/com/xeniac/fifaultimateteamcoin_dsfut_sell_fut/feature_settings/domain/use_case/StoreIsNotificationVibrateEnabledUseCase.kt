package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.utils.NotificationVibrateError

class StoreIsNotificationVibrateEnabledUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    suspend operator fun invoke(isEnabled: Boolean): Result<Unit, NotificationVibrateError> = try {
        settingsDataStoreRepository.isNotificationVibrateEnabled(isEnabled)
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(NotificationVibrateError.SomethingWentWrong)
    }
}
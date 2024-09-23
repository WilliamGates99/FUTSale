package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.utils.NotificationSoundError

class StoreIsNotificationSoundEnabledUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    suspend operator fun invoke(isEnabled: Boolean): Result<Unit, NotificationSoundError> = try {
        settingsDataStoreRepository.isNotificationSoundEnabled(isEnabled)
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(NotificationSoundError.SomethingWentWrong)
    }
}
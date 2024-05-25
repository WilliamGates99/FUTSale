package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.utils.NotificationSoundError

class SetIsNotificationSoundEnabledUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(isEnabled: Boolean): Result<Unit, NotificationSoundError> = try {
        preferencesRepository.isNotificationSoundEnabled(isEnabled)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(NotificationSoundError.SomethingWentWrong)
    }
}
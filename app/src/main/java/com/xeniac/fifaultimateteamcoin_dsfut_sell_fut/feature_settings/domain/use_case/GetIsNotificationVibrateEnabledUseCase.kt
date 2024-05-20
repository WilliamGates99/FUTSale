package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.PreferencesRepository

class GetIsNotificationVibrateEnabledUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): Boolean = preferencesRepository.isNotificationVibrateEnabled()
}
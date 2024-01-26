package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository

class SetIsNotificationVibrateActiveUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(isActive: Boolean) {
        preferencesRepository.isNotificationVibrateActive(isActive)
    }
}
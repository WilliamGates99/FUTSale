package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository

class GetNotificationPermissionCountUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): Int {
        return preferencesRepository.getNotificationPermissionCount()
    }
}
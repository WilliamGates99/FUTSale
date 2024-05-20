package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.PreferencesRepository

class SetNotificationPermissionCountUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(
        count: Int
    ) = preferencesRepository.setNotificationPermissionCount(count)
}
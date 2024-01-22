package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository

class GetIsOnboardingCompletedUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): Boolean {
        return preferencesRepository.isOnBoardingCompleted()
    }
}
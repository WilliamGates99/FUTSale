package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository

class SetIsOnboardingCompletedUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(isCompleted: Boolean) {
        preferencesRepository.isOnBoardingCompleted(isCompleted = isCompleted)
    }
}
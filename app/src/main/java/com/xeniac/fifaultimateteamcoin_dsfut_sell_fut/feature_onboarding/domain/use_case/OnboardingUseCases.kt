package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case

import dagger.Lazy

data class OnboardingUseCases(
    val setPartnerIdUseCase: Lazy<SetPartnerIdUseCase>,
    val setSecretKeyUseCase: Lazy<SetSecretKeyUseCase>,
    val setIsOnboardingCompletedUseCase: Lazy<SetIsOnboardingCompletedUseCase>
)
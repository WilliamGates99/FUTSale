package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case

import dagger.Lazy

data class MainUseCases(
    val getCurrentAppLocaleUseCase: Lazy<GetCurrentAppLocaleUseCase>,
    val getIsOnboardingCompletedUseCase: Lazy<GetIsOnboardingCompletedUseCase>
)
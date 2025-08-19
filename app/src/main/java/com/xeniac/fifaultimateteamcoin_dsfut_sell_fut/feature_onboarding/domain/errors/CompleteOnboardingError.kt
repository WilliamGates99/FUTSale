package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.errors

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.errors.Error

sealed class CompleteOnboardingError : Error() {
    data object SomethingWentWrong : CompleteOnboardingError()
}
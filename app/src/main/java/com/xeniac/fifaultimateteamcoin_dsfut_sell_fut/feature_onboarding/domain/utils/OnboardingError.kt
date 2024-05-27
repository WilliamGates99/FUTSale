package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Error

sealed class OnboardingError : Error() {
    data object SomethingWentWrong : OnboardingError()
}
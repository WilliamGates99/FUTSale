package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.util

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Event

sealed class OnboardingUiEvent : Event() {
    data object NavigateToHomeScreen : OnboardingUiEvent()
}
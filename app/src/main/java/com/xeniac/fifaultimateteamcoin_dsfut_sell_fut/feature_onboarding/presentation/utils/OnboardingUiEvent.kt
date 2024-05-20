package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event

sealed class OnboardingUiEvent : Event() {
    data object NavigateToHomeScreen : OnboardingUiEvent()
}
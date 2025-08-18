package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.events

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.Event

sealed class OnboardingUiEvent : Event() {
    data object NavigateToHomeScreen : OnboardingUiEvent()
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation

sealed class OnboardingEvent {
    data class PartnerIdChanged(val partnerId: String) : OnboardingEvent()
    data class SecretKeyChanged(val secretKey: String) : OnboardingEvent()
    data object SaveUserData : OnboardingEvent()
}
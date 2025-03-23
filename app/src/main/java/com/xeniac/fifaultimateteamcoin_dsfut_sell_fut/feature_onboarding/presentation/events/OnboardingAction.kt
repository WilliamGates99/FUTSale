package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.events

sealed interface OnboardingAction {
    data class PartnerIdChanged(val partnerId: String) : OnboardingAction
    data class SecretKeyChanged(val secretKey: String) : OnboardingAction
    data object SaveUserData : OnboardingAction
}
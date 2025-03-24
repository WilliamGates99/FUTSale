package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.events

import androidx.compose.ui.text.input.TextFieldValue

sealed interface OnboardingAction {
    data class PartnerIdChanged(val newValue: TextFieldValue) : OnboardingAction
    data class SecretKeyChanged(val newValue: TextFieldValue) : OnboardingAction
    data object SaveUserData : OnboardingAction
}
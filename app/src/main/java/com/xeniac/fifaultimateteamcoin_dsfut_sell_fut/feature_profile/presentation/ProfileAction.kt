package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation

import androidx.compose.ui.text.input.TextFieldValue

sealed interface ProfileAction {
    data object GetProfile : ProfileAction
    data class PartnerIdChanged(val newValue: TextFieldValue) : ProfileAction
    data class SecretKeyChanged(val newValue: TextFieldValue) : ProfileAction
}
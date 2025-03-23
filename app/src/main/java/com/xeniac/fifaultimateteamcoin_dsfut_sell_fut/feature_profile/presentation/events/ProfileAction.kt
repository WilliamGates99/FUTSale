package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.events

sealed interface ProfileAction {
    data object GetProfile : ProfileAction
    data class PartnerIdChanged(val partnerId: String) : ProfileAction
    data class SecretKeyChanged(val secretKey: String) : ProfileAction
}
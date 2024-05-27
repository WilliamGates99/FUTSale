package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation

sealed class ProfileEvent {
    data object GetProfile : ProfileEvent()
    data class PartnerIdChanged(val partnerId: String) : ProfileEvent()
    data class SecretKeyChanged(val secretKey: String) : ProfileEvent()
}
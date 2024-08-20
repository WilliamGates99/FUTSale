package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.states

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileState(
    val partnerId: String = "",
    val secretKey: String = "",
    val isPartnerIdSaved: Boolean? = null,
    val isSecretKeySaved: Boolean? = null,
    val partnerIdErrorText: UiText? = null,
    val secretKeyErrorText: UiText? = null,
    val isPartnerIdLoading: Boolean = false,
    val isSecretKeyLoading: Boolean = false
) : Parcelable
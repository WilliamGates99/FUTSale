package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.states

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.states.CustomTextFieldState
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileState(
    val partnerIdState: CustomTextFieldState = CustomTextFieldState(),
    val secretKeyState: CustomTextFieldState = CustomTextFieldState(),
    val isPartnerIdSaved: Boolean? = null,
    val isSecretKeySaved: Boolean? = null,
    val isPartnerIdLoading: Boolean = false,
    val isSecretKeyLoading: Boolean = false
) : Parcelable
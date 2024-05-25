package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.states

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnboardingState(
    val partnerId: String = "",
    val isPartnerIdValid: Boolean = false,
    val partnerIdErrorText: UiText? = null,
    val secretKey: String = "",
    val isSecretKeyValid: Boolean = false,
    val secretKeyErrorText: UiText? = null,
    val isCompleteLoading: Boolean = false
) : Parcelable
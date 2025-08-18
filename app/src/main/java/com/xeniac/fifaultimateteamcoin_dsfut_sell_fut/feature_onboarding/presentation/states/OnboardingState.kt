package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.states

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.states.CustomTextFieldState
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnboardingState(
    val partnerIdState: CustomTextFieldState = CustomTextFieldState(),
    val secretKeyState: CustomTextFieldState = CustomTextFieldState(),
    val isCompleteLoading: Boolean = false
) : Parcelable
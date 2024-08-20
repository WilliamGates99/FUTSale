package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.states

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomTextFieldState(
    val text: String = "",
    val isValid: Boolean = false,
    val errorText: UiText? = null
) : Parcelable
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.states

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.UiText
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class CustomTextFieldState(
    val text: String = "",
    val isValid: Boolean = false,
    val errorText: @RawValue UiText? = null
) : Parcelable
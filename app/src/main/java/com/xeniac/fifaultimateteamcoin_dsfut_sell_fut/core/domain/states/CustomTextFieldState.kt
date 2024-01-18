package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.states

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.UiText

data class CustomTextFieldState(
    val text: String = "",
    val isValid: Boolean = false,
    val errorText: UiText? = null
)
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.states

import android.os.Parcelable
import androidx.compose.ui.text.input.TextFieldValue
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiText
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

@Parcelize
@TypeParceler<TextFieldValue, TextFieldValueParceler>
data class CustomTextFieldState(
    val value: TextFieldValue = TextFieldValue(),
    val isValid: Boolean = false,
    val errorText: UiText? = null
) : Parcelable
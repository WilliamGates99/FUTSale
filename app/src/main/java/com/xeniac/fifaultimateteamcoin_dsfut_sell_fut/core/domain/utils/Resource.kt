package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText

sealed class Resource<T>(
    val data: T? = null,
    val message: UiText? = null
) {
    class Success<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: UiText, data: T? = null) : Resource<T>(data, message)
}
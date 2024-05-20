package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.UiText

interface PermissionHelper {
    fun getMessage(isPermanentlyDeclined: Boolean): UiText
}
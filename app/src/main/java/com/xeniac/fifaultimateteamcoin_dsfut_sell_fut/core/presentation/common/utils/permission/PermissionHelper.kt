package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.permission

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiText

interface PermissionHelper {
    fun getMessage(isPermanentlyDeclined: Boolean): UiText
}
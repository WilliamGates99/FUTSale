package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.util

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.permission.PermissionHelper

class PostNotificationsPermissionHelper : PermissionHelper {
    override fun getMessage(
        isPermanentlyDeclined: Boolean
    ): UiText {
        return when {
            isPermanentlyDeclined -> UiText.StringResource(R.string.permissions_error_notification_declined_permanently)
            else -> UiText.StringResource(R.string.permissions_error_notification_declined)
        }
    }
}
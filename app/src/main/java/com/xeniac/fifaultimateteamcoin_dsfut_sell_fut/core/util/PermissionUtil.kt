package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R

interface PermissionProvider {
    fun getMessage(isPermanentlyDeclined: Boolean): UiText
}

class PostNotificationsPermissionProvider : PermissionProvider {
    override fun getMessage(isPermanentlyDeclined: Boolean): UiText {
        return if (isPermanentlyDeclined) {
            UiText.StringResource(R.string.permissions_error_notification_declined_permanently)
        } else {
            UiText.StringResource(R.string.permissions_error_notification_declined)
        }
    }
}
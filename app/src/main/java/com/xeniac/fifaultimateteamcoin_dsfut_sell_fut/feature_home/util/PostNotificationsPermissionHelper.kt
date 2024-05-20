package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.util

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.PermissionHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.UiText

class PostNotificationsPermissionHelper : PermissionHelper {
    override fun getMessage(isPermanentlyDeclined: Boolean): UiText {
        return if (isPermanentlyDeclined) {
            UiText.StringResource(R.string.permissions_error_notification_declined_permanently)
        } else {
            UiText.StringResource(R.string.permissions_error_notification_declined)
        }
    }
}
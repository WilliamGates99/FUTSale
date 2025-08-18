package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.util

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.utils.StoreNotificationSoundError

fun StoreNotificationSoundError.asUiText(): UiText = when (this) {
    StoreNotificationSoundError.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}
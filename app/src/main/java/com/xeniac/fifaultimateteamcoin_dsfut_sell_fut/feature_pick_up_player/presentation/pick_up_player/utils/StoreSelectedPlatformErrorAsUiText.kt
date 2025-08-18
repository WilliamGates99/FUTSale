package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.StoreSelectedPlatformError

fun StoreSelectedPlatformError.asUiText(): UiText = when (this) {
    StoreSelectedPlatformError.SomethingWentWrong -> UiText.StringResource(R.string.error_something_went_wrong)
}
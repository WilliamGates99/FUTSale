package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Error

sealed class PlatformError : Error() {
    data object SomethingWentWrong : PlatformError()
}
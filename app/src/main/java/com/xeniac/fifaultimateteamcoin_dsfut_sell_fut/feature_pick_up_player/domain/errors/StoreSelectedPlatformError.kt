package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.errors

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.errors.Error

sealed class StoreSelectedPlatformError : Error() {
    data object SomethingWentWrong : StoreSelectedPlatformError()
}
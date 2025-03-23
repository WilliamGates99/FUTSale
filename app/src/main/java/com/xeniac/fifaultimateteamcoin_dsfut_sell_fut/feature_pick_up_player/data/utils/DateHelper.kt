package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper

object DateHelper {
    fun isPickedPlayerExpired(
        expiryTimeInMs: Long,
        currentTimeInMs: Long = DateHelper.getCurrentTimeInMillis()
    ): Boolean = currentTimeInMs > expiryTimeInMs
}
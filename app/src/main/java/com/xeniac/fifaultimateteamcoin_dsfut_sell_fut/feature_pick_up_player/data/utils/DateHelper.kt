package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.DateHelper

object DateHelper {
    fun isPickedPlayerNotExpired(pickUpTimeInMs: Long): Boolean {
        val expiryTimeInMs = pickUpTimeInMs + Constants.PLAYER_EXPIRY_TIME_IN_MS
        return DateHelper.getCurrentTimeInMillis() <= expiryTimeInMs
    }

    fun isPickedPlayerExpired(expiryTimeInMs: Long): Boolean {
        return expiryTimeInMs <= DateHelper.getCurrentTimeInMillis()
    }
}
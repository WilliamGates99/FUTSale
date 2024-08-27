package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.DateHelper

object DateHelper {
    fun isPickedPlayerNotExpired(pickUpTimeInSeconds: Long): Boolean {
        val expiryTimeInSeconds = pickUpTimeInSeconds + Constants.PLAYER_EXPIRY_TIME_IN_SECONDS
        return DateHelper.getCurrentTimeInSeconds() <= expiryTimeInSeconds
    }

    fun isPickedPlayerExpired(expiryTimeInSeconds: Long): Boolean {
        return expiryTimeInSeconds <= DateHelper.getCurrentTimeInSeconds()
    }
}
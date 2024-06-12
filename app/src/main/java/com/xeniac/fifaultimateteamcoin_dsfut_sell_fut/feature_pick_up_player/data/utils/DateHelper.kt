package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.DateHelper

object DateHelper {

    fun isPickedPlayerNotExpired(pickUpTimeInMillis: Long): Boolean {
        val expiryTimeInMillis = pickUpTimeInMillis + Constants.PLAYER_EXPIRY_TIME_IN_MS
        return DateHelper.getCurrentTimeInMillis() < expiryTimeInMillis
    }
}
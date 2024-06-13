package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils

import java.util.Calendar

object DateHelper {

    fun getCurrentTimeInMillis(): Long = Calendar.getInstance().timeInMillis

    fun isPickedPlayerNotExpired(pickUpTimeInMs: Long): Boolean {
        val expiryTimeInMillis = pickUpTimeInMs + Constants.PLAYER_EXPIRY_TIME_IN_MS
        return getCurrentTimeInMillis() < expiryTimeInMillis
    }

    fun isPickedPlayerExpired(expiryTimeInMs: Long): Boolean {
        return expiryTimeInMs <= getCurrentTimeInMillis()
    }
}
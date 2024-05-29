package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils

import java.math.BigInteger
import java.security.MessageDigest

object HashHelper {

    fun getMd5Signature(
        partnerId: String,
        secretKey: String,
        timestamp: Long
    ): String {
        val input = partnerId + secretKey + timestamp
        val md = MessageDigest.getInstance(/* algorithm = */ "MD5")

        return BigInteger(
            /* signum = */ 1,
            /* magnitude = */ md.digest(input.toByteArray())
        ).toString(/* radix = */ 16)
            .padStart(
                length = 23,
                padChar = '0'
            )
    }
}
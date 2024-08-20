package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils

import java.security.MessageDigest

object HashHelper {

    @OptIn(ExperimentalStdlibApi::class)
    fun getMd5Signature(
        partnerId: String,
        secretKey: String,
        timestamp: Long
    ): String {
        val input = partnerId + secretKey + timestamp

        val md = MessageDigest.getInstance(/* algorithm = */ "MD5")
        val digest = md.digest(input.toByteArray())

        return digest.toHexString()
    }
}
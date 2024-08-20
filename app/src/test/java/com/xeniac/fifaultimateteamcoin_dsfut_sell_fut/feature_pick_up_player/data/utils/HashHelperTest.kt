package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class HashHelperTest {

    @Test
    fun getMd5Signature_returnsMd5HashOfInput() {
        val partnerId = "123"
        val secretKey = "abc123"
        val timestampInSeconds = 1724157833L
        val expectedHash = "da75c5fee908145a6b73e08622637da1"

        val md5Hash = HashHelper.getMd5Signature(
            partnerId = partnerId,
            secretKey = secretKey,
            timestamp = timestampInSeconds
        )

        assertThat(md5Hash).isEqualTo(expectedHash)
    }
}
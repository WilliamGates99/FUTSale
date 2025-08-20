package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.HashGenerator
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.HashedString
import java.security.MessageDigest

class FakeMD5HashGenerator(
    private val md5MessageDigest: MessageDigest = MessageDigest.getInstance(/* algorithm = */ "MD5")
) : HashGenerator {

    override fun generateHash(
        input: String
    ): HashedString {
        val digest = md5MessageDigest.digest(input.toByteArray())
        return digest.toHexString()
    }
}
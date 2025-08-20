package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.HashGenerator
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.HashedString
import dagger.Lazy
import java.security.MessageDigest
import javax.inject.Inject

class MD5HashGenerator @Inject constructor(
    private val md5MessageDigest: Lazy<MessageDigest>
) : HashGenerator {

    override fun generateHash(
        input: String
    ): HashedString {
        val digest = md5MessageDigest.get().digest(input.toByteArray())
        return digest.toHexString()
    }
}
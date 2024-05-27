package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.SecretKeyError

class ValidateSecretKey {
    operator fun invoke(secretKey: String): SecretKeyError? {
        if (secretKey.isBlank()) {
            return SecretKeyError.BlankSecretKey
        }

        return null
    }
}
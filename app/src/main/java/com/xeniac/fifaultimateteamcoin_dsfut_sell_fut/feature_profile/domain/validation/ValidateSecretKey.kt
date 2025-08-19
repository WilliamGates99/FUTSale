package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.errors.UpdateSecretKeyError

class ValidateSecretKey {
    operator fun invoke(
        secretKey: String
    ): UpdateSecretKeyError? {
        return null
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError

class ValidateSecretKey {
    operator fun invoke(secretKey: String?): PickUpPlayerError? {
        if (secretKey.isNullOrBlank()) {
            return PickUpPlayerError.BlankSecretKey
        }

        return null
    }
}
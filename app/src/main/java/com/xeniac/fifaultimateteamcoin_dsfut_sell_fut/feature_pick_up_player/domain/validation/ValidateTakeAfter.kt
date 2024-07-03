package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError

class ValidateTakeAfter {
    operator fun invoke(takeAfter: Int?): PickUpPlayerError? {
        if (takeAfter == null) {
            return PickUpPlayerError.InvalidTakeAfter
        }

        return null
    }
}
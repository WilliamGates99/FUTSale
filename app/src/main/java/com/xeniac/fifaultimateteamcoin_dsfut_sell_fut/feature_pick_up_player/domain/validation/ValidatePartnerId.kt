package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation

import androidx.core.text.isDigitsOnly
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError

class ValidatePartnerId {
    operator fun invoke(partnerId: String?): PickUpPlayerError? {
        if (partnerId.isNullOrBlank()) {
            return PickUpPlayerError.BlankPartnerId
        }

        if (!partnerId.isDigitsOnly()) {
            return PickUpPlayerError.InvalidPartnerId
        }

        return null
    }
}
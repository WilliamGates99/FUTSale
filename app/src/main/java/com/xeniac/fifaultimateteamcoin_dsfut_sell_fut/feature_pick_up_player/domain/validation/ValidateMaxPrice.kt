package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation

import androidx.core.text.isDigitsOnly
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError

class ValidateMaxPrice {
    operator fun invoke(maxPrice: String?): PickUpPlayerError? {
        if (!maxPrice.isNullOrBlank() && !maxPrice.isDigitsOnly()) {
            return PickUpPlayerError.InvalidMaxPrice
        }

        return null
    }
}
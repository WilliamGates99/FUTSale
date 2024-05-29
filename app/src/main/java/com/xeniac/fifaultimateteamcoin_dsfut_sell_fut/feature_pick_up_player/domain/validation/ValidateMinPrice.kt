package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation

import androidx.core.text.isDigitsOnly
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError

class ValidateMinPrice {
    operator fun invoke(minPrice: String?): PickUpPlayerError? {
        if (!minPrice.isNullOrBlank() && !minPrice.isDigitsOnly()) {
            return PickUpPlayerError.InvalidMinPrice
        }

        return null
    }
}
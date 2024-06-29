package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError

class ValidateMinPrice {
    operator fun invoke(minPrice: String?): PickUpPlayerError? {
        if (!minPrice.isNullOrBlank() && minPrice.any { !it.isDigit() }) {
            return PickUpPlayerError.InvalidMinPrice
        }

        return null
    }
}
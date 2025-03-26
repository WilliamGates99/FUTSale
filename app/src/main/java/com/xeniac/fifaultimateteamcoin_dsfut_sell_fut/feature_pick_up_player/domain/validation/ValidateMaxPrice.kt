package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError

class ValidateMaxPrice {
    operator fun invoke(maxPrice: String?): PickUpPlayerError? {
        val isMaxPriceBlank = maxPrice.isNullOrBlank()
        val doesMaxPriceContainNonDigits = !isMaxPriceBlank && maxPrice!!.any { !it.isDigit() }
        if (doesMaxPriceContainNonDigits) {
            return PickUpPlayerError.InvalidMaxPrice
        }

        return null
    }
}
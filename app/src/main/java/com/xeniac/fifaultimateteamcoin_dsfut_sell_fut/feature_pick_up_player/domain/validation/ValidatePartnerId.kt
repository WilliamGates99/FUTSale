package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.errors.PickUpPlayerError

class ValidatePartnerId {
    operator fun invoke(
        partnerId: String?
    ): PickUpPlayerError? {
        if (partnerId.isNullOrBlank()) {
            return PickUpPlayerError.BlankPartnerId
        }

        val doesPartnerIdContainNonDigits = partnerId.any { !it.isDigit() }
        if (doesPartnerIdContainNonDigits) {
            return PickUpPlayerError.InvalidPartnerId
        }

        return null
    }
}
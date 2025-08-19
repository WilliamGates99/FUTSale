package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.errors.UpdatePartnerIdError

class ValidatePartnerId {
    operator fun invoke(
        partnerId: String
    ): UpdatePartnerIdError? {
        val isPartnerIdBlank = partnerId.isBlank()
        val doesPartnerIdContainNonDigits = !isPartnerIdBlank && partnerId.any { !it.isDigit() }
        if (doesPartnerIdContainNonDigits) {
            return UpdatePartnerIdError.InvalidPartnerId
        }

        return null
    }
}
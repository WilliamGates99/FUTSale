package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.UpdatePartnerIdError

class ValidatePartnerId {
    operator fun invoke(partnerId: String): UpdatePartnerIdError? {
        val doesPartnerIdContainNonDigits = partnerId.isNotBlank()
                && partnerId.any { !it.isDigit() }
        if (doesPartnerIdContainNonDigits) {
            return UpdatePartnerIdError.InvalidPartnerId
        }

        return null
    }
}
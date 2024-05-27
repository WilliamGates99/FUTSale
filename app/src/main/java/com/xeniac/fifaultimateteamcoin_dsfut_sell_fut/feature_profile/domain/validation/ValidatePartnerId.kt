package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.PartnerIdError

class ValidatePartnerId {
    operator fun invoke(partnerId: String): PartnerIdError? {
        if (partnerId.isBlank()) {
            return PartnerIdError.BlankPartnerId
        }

        return null
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation

import androidx.core.text.isDigitsOnly
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.PartnerIdError

class ValidatePartnerId {
    operator fun invoke(partnerId: String): PartnerIdError? {
        if (partnerId.isNotBlank() && !partnerId.isDigitsOnly()) {
            return PartnerIdError.InvalidPartnerId
        }

        return null
    }
}
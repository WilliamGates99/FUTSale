package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Error

sealed class PartnerIdError : Error() {
    data object SomethingWentWrong : PartnerIdError()
    data object InvalidPartnerId : PartnerIdError()
}
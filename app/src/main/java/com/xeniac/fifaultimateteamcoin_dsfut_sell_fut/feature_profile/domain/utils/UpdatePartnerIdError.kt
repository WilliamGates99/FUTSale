package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Error

sealed class UpdatePartnerIdError : Error() {
    data object InvalidPartnerId : UpdatePartnerIdError()
    data object SomethingWentWrong : UpdatePartnerIdError()
}
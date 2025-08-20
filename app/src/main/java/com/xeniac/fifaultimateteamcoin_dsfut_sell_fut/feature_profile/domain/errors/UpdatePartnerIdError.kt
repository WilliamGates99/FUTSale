package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.errors

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.errors.Error

sealed class UpdatePartnerIdError : Error() {
    data object InvalidPartnerId : UpdatePartnerIdError()
    data object SomethingWentWrong : UpdatePartnerIdError()
}
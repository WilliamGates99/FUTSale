package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Error

sealed class UpdateSecretKeyError : Error() {
    data object InvalidSecretKey : UpdateSecretKeyError()
    data object SomethingWentWrong : UpdateSecretKeyError()
}
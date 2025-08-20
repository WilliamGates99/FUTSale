package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.errors

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.errors.Error

sealed class StoreAppLocaleError : Error() {
    data object SomethingWentWrong : StoreAppLocaleError()
}
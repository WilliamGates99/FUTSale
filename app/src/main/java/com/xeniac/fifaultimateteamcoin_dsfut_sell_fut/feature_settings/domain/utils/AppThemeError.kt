package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.utils

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Error

sealed class AppThemeError : Error() {
    data object SomethingWentWrong : AppThemeError()
}
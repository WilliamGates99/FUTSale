package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.main_activity.states

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale

data class MainActivityState(
    val currentAppLocale: AppLocale = AppLocale.Default,
    val isSplashScreenLoading: Boolean = true,
    val postSplashDestination: Any? = null
)
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.states

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainActivityState(
    val currentAppLocale: AppLocale = AppLocale.Default,
    val isSplashScreenLoading: Boolean = true,
    val postSplashDestination: Screen? = null
) : Parcelable
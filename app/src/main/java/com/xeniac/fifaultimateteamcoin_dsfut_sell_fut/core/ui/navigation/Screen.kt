package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
sealed class Screen : Parcelable {

    @Serializable
    data object OnboardingScreen : Screen()

    @Serializable
    data object HomeScreen : Screen()

    @Serializable
    data object PickUpScreen : Screen()

    @Serializable
    data object ProfileScreen : Screen()

    @Serializable
    data object HistoryScreen : Screen()

    @Serializable
    data object SettingsScreen : Screen()
}
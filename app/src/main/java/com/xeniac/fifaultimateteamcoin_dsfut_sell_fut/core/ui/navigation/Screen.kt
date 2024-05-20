package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

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
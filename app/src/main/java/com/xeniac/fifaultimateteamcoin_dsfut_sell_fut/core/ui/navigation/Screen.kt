package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation

sealed class Screen(val route: String) {
    data object OnboardingScreen : Screen(route = "onboarding_screen")

    data object HomeScreen : Screen(route = "home_screen")

    data object PickUpScreen : Screen(route = "pick_up_screen")
    data object ProfileScreen : Screen(route = "profile_screen")
    data object HistoryScreen : Screen(route = "history_screen")
    data object SettingsScreen : Screen(route = "settings_screen")

    fun withArgs(vararg args: Any): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.HomeScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.OnboardingScreen

@Composable
fun SetupRootNavGraph(
    rootNavController: NavHostController,
    startDestination: Screen
) {
    NavHost(
        navController = rootNavController,
        startDestination = startDestination
    ) {
        composable<Screen.OnboardingScreen> {
            OnboardingScreen(
                onNavigateToHomeScreen = {
                    rootNavController.navigate(Screen.HomeScreen) {
                        popUpTo(Screen.OnboardingScreen) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Screen.HomeScreen> {
            HomeScreen()
        }
    }
}
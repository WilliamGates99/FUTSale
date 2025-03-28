package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.navigation.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.navigation.HomeScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.ui.navigation.OnboardingScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.presentation.HomeScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.OnboardingScreen

@Composable
fun SetupRootNavGraph(
    rootNavController: NavHostController,
    startDestination: Any
) {
    NavHost(
        navController = rootNavController,
        startDestination = startDestination
    ) {
        composable<OnboardingScreen> {
            OnboardingScreen(
                onNavigateToHomeScreen = {
                    rootNavController.navigate(HomeScreen) {
                        popUpTo(OnboardingScreen) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<HomeScreen> {
            HomeScreen()
        }
    }
}
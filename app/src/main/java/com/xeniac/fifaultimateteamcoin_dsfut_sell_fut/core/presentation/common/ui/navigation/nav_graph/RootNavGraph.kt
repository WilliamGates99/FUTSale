package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.HomeScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.OnboardingScreen
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
        composable<com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.OnboardingScreen> {
            OnboardingScreen(
                onNavigateToHomeScreen = {
                    rootNavController.navigate(com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.HomeScreen) {
                        popUpTo(com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.OnboardingScreen) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.HomeScreen> {
            HomeScreen()
        }
    }
}
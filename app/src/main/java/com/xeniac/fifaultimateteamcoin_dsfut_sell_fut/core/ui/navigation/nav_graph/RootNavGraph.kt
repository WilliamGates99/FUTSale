package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.OnboardingScreen

@Composable
fun SetupRootNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = NavGraphs.ROUTE_ROOT
    ) {
        composable(route = Screen.OnboardingScreen.route) {
            OnboardingScreen()
        }

        composable(route = Screen.HomeScreen.route) {
            // MainScreen(rootNavController = navController)
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}

object NavGraphs {
    const val ROUTE_ROOT = "nav_graph_root"
    const val ROUTE_HOME = "nav_graph_home"
}
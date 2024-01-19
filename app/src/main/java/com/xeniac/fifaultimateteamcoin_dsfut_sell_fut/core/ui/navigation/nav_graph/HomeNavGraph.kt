package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen

@Composable
fun SetupHomeNavGraph(
    rootNavController: NavHostController,
    homeNavController: NavHostController,
) {
    NavHost(
        navController = homeNavController,
        startDestination = Screen.PickUpScreen.route,
        route = NavGraphs.ROUTE_HOME
    ) {
        composable(route = Screen.PickUpScreen.route) {
            // PickUpScreen()
        }
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.HistoryScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.PickUpScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.ProfileScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.SettingsScreen

@Composable
fun SetupHomeNavGraph(
    rootNavController: NavHostController,
    homeNavController: NavHostController,
    bottomPadding: Dp
) {
    NavHost(
        navController = homeNavController,
        startDestination = Screen.PickUpScreen
    ) {
        composable<Screen.PickUpScreen> {
            PickUpScreen()
        }

        composable<Screen.ProfileScreen> {
            ProfileScreen()
        }

        composable<Screen.HistoryScreen> {
            HistoryScreen()
        }

        composable<Screen.SettingsScreen> {
            SettingsScreen(bottomPadding = bottomPadding)
        }
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.nav_graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.screens.PickUpPlayerScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.screens.ProfileScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.screens.SettingsScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.ProfileScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.SettingsScreen

@Composable
fun SetupHomeNavGraph(
    homeNavController: NavHostController,
    bottomPadding: Dp
) {
    NavHost(
        navController = homeNavController,
        startDestination = PickUpPlayerScreen
    ) {
        pickUpPlayerNavGraph(
            homeNavController = homeNavController,
            bottomPadding = bottomPadding
        )

        composable<ProfileScreen> {
            ProfileScreen(bottomPadding = bottomPadding)
        }

        historyNavGraph(
            homeNavController = homeNavController,
            bottomPadding = bottomPadding
        )

        composable<SettingsScreen> {
            SettingsScreen(bottomPadding = bottomPadding)
        }
    }
}
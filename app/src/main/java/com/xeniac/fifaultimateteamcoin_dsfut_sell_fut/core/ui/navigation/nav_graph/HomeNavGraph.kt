package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.utils.PlayerCustomNavType
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.HistoryScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.PickUpPlayerScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info.PickedUpPlayerInfoScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.ProfileScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.SettingsScreen
import kotlin.reflect.typeOf

@Composable
fun SetupHomeNavGraph(
    homeNavController: NavHostController,
    bottomPadding: Dp
) {
    NavHost(
        navController = homeNavController,
        startDestination = Screen.PickUpPlayerScreen
    ) {
        // TODO: MOVE TO SEPARATE NAV_GRAPH
        composable<Screen.PickUpPlayerScreen> {
            PickUpPlayerScreen(
                bottomPadding = bottomPadding,
                onNavigateToPickedUpPlayerInfoScreen = { player ->
                    homeNavController.navigate(Screen.PickedUpPlayerInfoScreen(player = player))
                }
            )
        }

        composable<Screen.PickedUpPlayerInfoScreen>(
            typeMap = mapOf(typeOf<Player>() to PlayerCustomNavType)
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.PickedUpPlayerInfoScreen>()

            PickedUpPlayerInfoScreen(
                player = args.player
            )
        }

        composable<Screen.ProfileScreen> {
            ProfileScreen(bottomPadding = bottomPadding)
        }

        composable<Screen.HistoryScreen> {
            HistoryScreen()
        }

        composable<Screen.SettingsScreen> {
            SettingsScreen(bottomPadding = bottomPadding)
        }
    }
}
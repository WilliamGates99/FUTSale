package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.utils.PlayerCustomNavType
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.PickUpPlayerScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info.PickedUpPlayerInfoScreen
import kotlin.reflect.typeOf

fun NavGraphBuilder.pickUpPlayerNavGraph(
    homeNavController: NavHostController,
    bottomPadding: Dp
) {
    composable<Screen.PickUpPlayerScreen> {
        PickUpPlayerScreen(
            bottomPadding = bottomPadding,
            onNavigateToProfileScreen = {
                homeNavController.navigate(Screen.ProfileScreen) {
                    launchSingleTop = true
                    popUpTo(Screen.PickUpPlayerScreen)
                }
            },
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
}
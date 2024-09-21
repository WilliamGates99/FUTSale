package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.PickUpPlayerScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.PickedUpPlayerInfoScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.ProfileScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.utils.PlayerCustomNavType
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.PickUpPlayerScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info.PickedUpPlayerInfoScreen
import kotlin.reflect.typeOf

fun NavGraphBuilder.pickUpPlayerNavGraph(
    homeNavController: NavHostController,
    bottomPadding: Dp
) {
    composable<PickUpPlayerScreen> {
        PickUpPlayerScreen(
            bottomPadding = bottomPadding,
            onNavigateToProfileScreen = {
                homeNavController.navigate(ProfileScreen) {
                    launchSingleTop = true
                    popUpTo(homeNavController.graph.startDestinationId)
                }
            },
            onNavigateToPickedUpPlayerInfoScreen = { player ->
                homeNavController.navigate(PickedUpPlayerInfoScreen(player = player))
            }
        )
    }

    composable<PickedUpPlayerInfoScreen>(
        typeMap = mapOf(typeOf<Player>() to PlayerCustomNavType)
    ) {
        PickedUpPlayerInfoScreen(
            onNavigateUp = homeNavController::navigateUp
        )
    }
}
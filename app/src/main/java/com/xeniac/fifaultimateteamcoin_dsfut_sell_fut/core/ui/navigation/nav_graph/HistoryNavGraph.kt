package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.Screen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.utils.PlayerCustomNavType
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history.HistoryScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.HistoryPlayerInfoScreen
import kotlin.reflect.typeOf

fun NavGraphBuilder.historyNavGraph(
    homeNavController: NavHostController,
    bottomPadding: Dp
) {
    composable<Screen.HistoryScreen> {
        HistoryScreen(
            bottomPadding = bottomPadding,
            onNavigateToPlayerInfoScreen = { player ->
                homeNavController.navigate(Screen.HistoryPlayerInfoScreen(player = player))
            }
        )
    }

    composable<Screen.HistoryPlayerInfoScreen>(
        typeMap = mapOf(typeOf<Player>() to PlayerCustomNavType)
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<Screen.HistoryPlayerInfoScreen>()

        HistoryPlayerInfoScreen(
            player = args.player,
            onNavigateUp = homeNavController::navigateUp
        )
    }
}
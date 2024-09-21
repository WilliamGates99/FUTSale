package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.nav_graph

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.HistoryPlayerInfoScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.ui.navigation.HistoryScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history.HistoryScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.HistoryPlayerInfoScreen

fun NavGraphBuilder.historyNavGraph(
    homeNavController: NavHostController,
    bottomPadding: Dp
) {
    composable<HistoryScreen> {
        HistoryScreen(
            bottomPadding = bottomPadding,
            onNavigateToPlayerInfoScreen = { playerId ->
                homeNavController.navigate(HistoryPlayerInfoScreen(playerId))
            }
        )
    }

    composable<HistoryPlayerInfoScreen> {
        HistoryPlayerInfoScreen(
            onNavigateUp = homeNavController::navigateUp
        )
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.nav_graph

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.HistoryPlayerInfoScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.HistoryScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.history.HistoryScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info.HistoryPlayerInfoScreen

fun NavGraphBuilder.historyNavGraph(
    homeNavController: NavHostController,
    bottomPadding: Dp
) {
    composable<com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.HistoryScreen> {
        HistoryScreen(
            bottomPadding = bottomPadding,
            onNavigateToPlayerInfoScreen = { playerId ->
                homeNavController.navigate(
                    com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.HistoryPlayerInfoScreen(
                        playerId
                    )
                )
            }
        )
    }

    composable<com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.HistoryPlayerInfoScreen> {
        HistoryPlayerInfoScreen(
            onNavigateUp = homeNavController::navigateUp
        )
    }
}
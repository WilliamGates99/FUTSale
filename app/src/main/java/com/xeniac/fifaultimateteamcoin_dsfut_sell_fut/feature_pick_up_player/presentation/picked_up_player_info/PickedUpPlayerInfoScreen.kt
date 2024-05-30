package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player

@Composable
fun PickedUpPlayerInfoScreen(
    player: Player,
    viewModel: PickedUpPlayerInfoViewModel = hiltViewModel()
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "PickedUpPlayerInfoScreen. Player = $player")
    }
}
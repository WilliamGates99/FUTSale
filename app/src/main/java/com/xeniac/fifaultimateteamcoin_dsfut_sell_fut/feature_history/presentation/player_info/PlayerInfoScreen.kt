package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.presentation.player_info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player

@Composable
fun PlayerInfoScreen(
    player: Player,
    onNavigateUp: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "player = $player")
    }
}
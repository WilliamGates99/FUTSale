package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player

import android.view.WindowManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.findActivity

@Composable
fun PickUpPlayerScreen(
    bottomPadding: Dp,
    onNavigateToPickedUpPlayerInfoScreen: (player: Player) -> Unit,
    viewModel: PickUpPlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val threeLatestPlayers by viewModel.observeThreeLatestPlayers().collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val pickUpPlayerState by viewModel.pickUpPlayerState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = pickUpPlayerState.isAutoPickUpLoading) {
        val window = context.findActivity().window

        when (pickUpPlayerState.isAutoPickUpLoading) {
            true -> window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            false -> window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Pick Up Player Screen",
            color = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
    }
}
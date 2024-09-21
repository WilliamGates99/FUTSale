package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.PickUpPlayerAction
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.states.PickUpPlayerState

@Composable
fun PickUpPlayerSection(
    pickUpPlayerState: PickUpPlayerState,
    modifier: Modifier = Modifier,
    onAction: (action: PickUpPlayerAction) -> Unit
) {
    Column(modifier = modifier) {
        InstructionTexts(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(24.dp))

        PlatformSelector(
            pickUpPlayerState = pickUpPlayerState,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))

        PriceTextFields(
            pickUpPlayerState = pickUpPlayerState,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        TakeAfterSlider(
            pickUpPlayerState = pickUpPlayerState,
            modifier = Modifier.fillMaxWidth(),
            onAction = onAction
        )

        Spacer(modifier = Modifier.height(40.dp))

        AutoPickUpButton(
            pickUpPlayerState = pickUpPlayerState,
            onAction = onAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        PickUpOnceButton(
            pickUpPlayerState = pickUpPlayerState,
            onClick = {
                onAction(PickUpPlayerAction.PickUpPlayerOnce)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )
    }
}
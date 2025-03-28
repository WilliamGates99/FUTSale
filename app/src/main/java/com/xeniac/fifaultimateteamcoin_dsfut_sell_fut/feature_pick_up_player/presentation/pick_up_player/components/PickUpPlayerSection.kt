package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.events.PickUpPlayerAction
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
            isAutoPickUpLoading = pickUpPlayerState.isAutoPickUpLoading,
            isPickUpOnceLoading = pickUpPlayerState.isPickUpOnceLoading,
            selectedPlatform = pickUpPlayerState.selectedPlatform,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))

        PriceTextFields(
            isAutoPickUpLoading = pickUpPlayerState.isAutoPickUpLoading,
            isPickUpOnceLoading = pickUpPlayerState.isPickUpOnceLoading,
            minPriceState = pickUpPlayerState.minPriceState,
            maxPriceState = pickUpPlayerState.maxPriceState,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        TakeAfterSlider(
            isAutoPickUpLoading = pickUpPlayerState.isAutoPickUpLoading,
            isPickUpOnceLoading = pickUpPlayerState.isPickUpOnceLoading,
            isTakeAfterChecked = pickUpPlayerState.isTakeAfterChecked,
            takeAfterDelayInSeconds = pickUpPlayerState.takeAfterDelayInSeconds,
            takeAfterErrorText = pickUpPlayerState.takeAfterErrorText,
            modifier = Modifier.fillMaxWidth(),
            onAction = onAction
        )

        Spacer(modifier = Modifier.height(40.dp))

        AutoPickUpButton(
            isAutoPickUpLoading = pickUpPlayerState.isAutoPickUpLoading,
            onAction = onAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        PickUpOnceButton(
            isPickUpOnceLoading = pickUpPlayerState.isPickUpOnceLoading,
            onClick = {
                onAction(PickUpPlayerAction.PickUpPlayerOnce)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )
    }
}
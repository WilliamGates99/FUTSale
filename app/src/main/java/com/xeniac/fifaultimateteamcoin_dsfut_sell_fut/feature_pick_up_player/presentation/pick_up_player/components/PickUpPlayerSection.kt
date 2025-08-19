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
    state: PickUpPlayerState,
    modifier: Modifier = Modifier,
    onAction: (action: PickUpPlayerAction) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        InstructionTexts()

        Spacer(modifier = Modifier.height(24.dp))

        PlatformSelector(
            isAutoPickUpLoading = state.isAutoPickUpLoading,
            isPickUpOnceLoading = state.isPickUpOnceLoading,
            selectedPlatform = state.selectedPlatform,
            onAction = onAction
        )

        Spacer(modifier = Modifier.height(18.dp))

        PriceTextFields(
            isAutoPickUpLoading = state.isAutoPickUpLoading,
            isPickUpOnceLoading = state.isPickUpOnceLoading,
            minPriceState = state.minPriceState,
            maxPriceState = state.maxPriceState,
            onAction = onAction
        )

        Spacer(modifier = Modifier.height(14.dp))

        TakeAfterSlider(
            isAutoPickUpLoading = state.isAutoPickUpLoading,
            isPickUpOnceLoading = state.isPickUpOnceLoading,
            isTakeAfterChecked = state.isTakeAfterChecked,
            takeAfterDelayInSeconds = state.takeAfterDelayInSeconds,
            takeAfterErrorText = state.takeAfterErrorText,
            onAction = onAction
        )

        Spacer(modifier = Modifier.height(40.dp))

        AutoPickUpButton(
            isAutoPickUpLoading = state.isAutoPickUpLoading,
            onAction = onAction,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        PickUpOnceButton(
            isPickUpOnceLoading = state.isPickUpOnceLoading,
            onClick = {
                onAction(PickUpPlayerAction.PickUpPlayerOnce)
            },
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}
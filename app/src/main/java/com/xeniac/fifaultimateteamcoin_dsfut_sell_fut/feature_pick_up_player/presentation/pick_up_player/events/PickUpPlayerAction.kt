package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.events

import androidx.compose.ui.text.input.TextFieldValue
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform

sealed interface PickUpPlayerAction {
    data class PlatformChanged(val platform: Platform) : PickUpPlayerAction
    data class MinPriceChanged(val newValue: TextFieldValue) : PickUpPlayerAction
    data class MaxPriceChanged(val newValue: TextFieldValue) : PickUpPlayerAction
    data class TakeAfterCheckedChanged(val isChecked: Boolean) : PickUpPlayerAction
    data class TakeAfterSliderChanged(val delayInSeconds: Int) : PickUpPlayerAction

    data object CancelAutoPickUpPlayer : PickUpPlayerAction
    data object AutoPickUpPlayer : PickUpPlayerAction
    data object PickUpPlayerOnce : PickUpPlayerAction

    data class StartCountDownTimer(val expiryTimeInMs: Long) : PickUpPlayerAction
}
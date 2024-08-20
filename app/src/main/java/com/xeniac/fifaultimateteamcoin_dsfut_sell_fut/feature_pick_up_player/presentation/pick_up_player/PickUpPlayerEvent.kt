package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform

sealed interface PickUpPlayerEvent {
    data object GetPersistedData : PickUpPlayerEvent
    data class PlatformChanged(val platform: Platform) : PickUpPlayerEvent
    data class MinPriceChanged(val minPrice: String) : PickUpPlayerEvent
    data class MaxPriceChanged(val maxPrice: String) : PickUpPlayerEvent
    data class TakeAfterCheckedChanged(val isChecked: Boolean) : PickUpPlayerEvent
    data class TakeAfterSliderChanged(val delayInSeconds: Int) : PickUpPlayerEvent

    data object CancelAutoPickUpPlayer : PickUpPlayerEvent
    data object AutoPickUpPlayer : PickUpPlayerEvent
    data object PickUpPlayerOnce : PickUpPlayerEvent

    data class StartCountDownTimer(val expiryTimeInMs: Long) : PickUpPlayerEvent
}
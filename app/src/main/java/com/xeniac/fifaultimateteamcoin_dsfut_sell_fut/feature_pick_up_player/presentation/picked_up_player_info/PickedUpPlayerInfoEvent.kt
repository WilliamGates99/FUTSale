package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info

sealed class PickedUpPlayerInfoEvent {
    data class StartCountDownTimer(val expiryTimeInMs: Long) : PickedUpPlayerInfoEvent()
}
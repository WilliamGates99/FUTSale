package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info.events

sealed class PickedUpPlayerInfoAction {
    data class StartCountDownTimer(val expiryTimeInMs: Long) : PickedUpPlayerInfoAction()
}
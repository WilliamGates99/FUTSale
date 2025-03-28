package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories

import kotlinx.coroutines.flow.Flow

typealias TimerValueInSeconds = Int

interface CountDownTimerRepository {
    fun observeCountDownTimer(
        expiryTimeInMs: Long
    ): Flow<TimerValueInSeconds>
}
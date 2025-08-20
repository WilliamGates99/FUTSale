package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.DateHelper.isPickedPlayerExpired
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.CountDownTimerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.TimerValueInSeconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeCountDownTimerRepositoryImpl @Inject constructor() : CountDownTimerRepository {

    override fun observeCountDownTimer(
        expiryTimeInMs: Long
    ): Flow<TimerValueInSeconds> = flow {
        val currentTime = DateHelper.getCurrentTimeInMillis()
        val expiryTime = currentTime + expiryTimeInMs
        val isPlayerExpired = isPickedPlayerExpired(expiryTime)

        if (isPlayerExpired) {
            emit(0)
            return@flow
        }

        val timerStartTimeInMs = expiryTime - currentTime
        var currentTimerValueInMs = timerStartTimeInMs

        while (currentTimerValueInMs >= 0) {
            emit((currentTimerValueInMs / 1000).toInt())
            delay(timeMillis = Constants.COUNT_DOWN_TIMER_INTERVAL_IN_MS)
            currentTimerValueInMs -= Constants.COUNT_DOWN_TIMER_INTERVAL_IN_MS
        }
    }
}
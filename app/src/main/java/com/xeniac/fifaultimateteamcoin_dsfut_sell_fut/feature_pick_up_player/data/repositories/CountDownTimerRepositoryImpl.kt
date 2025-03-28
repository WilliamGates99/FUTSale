package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories

import android.os.CountDownTimer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.DateHelper.isPickedPlayerExpired
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.CountDownTimerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.TimerValueInSeconds
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class CountDownTimerRepositoryImpl @Inject constructor() : CountDownTimerRepository {

    override fun observeCountDownTimer(
        expiryTimeInMs: Long
    ): Flow<TimerValueInSeconds> = callbackFlow {
        val isPlayerExpired = isPickedPlayerExpired(expiryTimeInMs)
        if (isPlayerExpired) {
            send(0)
            close()
            return@callbackFlow
        }

        val timerStartTimeInMs = expiryTimeInMs - DateHelper.getCurrentTimeInMillis()
        val countDownTimer = object : CountDownTimer(
            /* millisInFuture = */ timerStartTimeInMs,
            /* countDownInterval = */ Constants.COUNT_DOWN_TIMER_INTERVAL_IN_MS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                launch { send((millisUntilFinished / 1000).toInt()) }
            }

            override fun onFinish() {
                Timber.i("Player Expiry timer is finished.")
                launch { send(0) }
                close()
            }
        }.start()

        awaitClose {
            countDownTimer?.cancel()
        }
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.TimerValueInSeconds
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePickUpPlayerRepositoryImpl : PickUpPlayerRepository {

    override fun observeLatestPickedPlayers(): Flow<List<Player>> {
        TODO("Not yet implemented")
    }

    override fun observeCountDownTimer(expiryTimeInMs: Long): Flow<TimerValueInSeconds> = flow {
        val currentTime = DateHelper.getCurrentTimeInMillis()
        val expiryTime = currentTime + expiryTimeInMs
        val isPlayerExpired = DateHelper.isPickedPlayerExpired(expiryTime)

        if (isPlayerExpired) {
            emit(0)
        } else {
            val timerStartTimeInMs = expiryTime - currentTime
            var currentTimerValueInMs = timerStartTimeInMs

            while (currentTimerValueInMs >= 0) {
                emit((currentTimerValueInMs / 1000).toInt())
                delay(timeMillis = Constants.COUNT_DOWN_TIMER_INTERVAL_IN_MS)
                currentTimerValueInMs -= Constants.COUNT_DOWN_TIMER_INTERVAL_IN_MS
            }
        }
    }

    override suspend fun pickUpPlayer(
        partnerId: String,
        secretKey: String,
        minPrice: String?,
        maxPrice: String?,
        takeAfterDelayInSeconds: Int?
    ): Result<Player, PickUpPlayerError> {
        TODO("Not yet implemented")
    }
}
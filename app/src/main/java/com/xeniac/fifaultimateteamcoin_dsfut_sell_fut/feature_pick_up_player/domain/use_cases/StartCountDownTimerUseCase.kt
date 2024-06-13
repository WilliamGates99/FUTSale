package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.TimerValueInSeconds
import kotlinx.coroutines.flow.Flow

class StartCountDownTimerUseCase(
    private val pickUpPlayerRepository: PickUpPlayerRepository
) {
    operator fun invoke(expiryTimeInMs: Long): Flow<TimerValueInSeconds> =
        pickUpPlayerRepository.observeCountDownTimer(expiryTimeInMs)
}
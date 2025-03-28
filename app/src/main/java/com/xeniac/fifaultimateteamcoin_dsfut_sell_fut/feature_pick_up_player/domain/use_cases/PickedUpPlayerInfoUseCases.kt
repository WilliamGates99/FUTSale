package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import dagger.Lazy

data class PickedUpPlayerInfoUseCases(
    val observePickedUpPlayerUseCase: Lazy<ObservePickedUpPlayerUseCase>,
    val startCountDownTimerUseCase: Lazy<StartCountDownTimerUseCase>
)
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import dagger.Lazy

data class PickUpPlayerUseCases(
    val observeThreeLatestPlayersUseCase: Lazy<ObserveThreeLatestPlayersUseCase>,
    val getSelectedPlatformUseCase: Lazy<GetSelectedPlatformUseCase>,
    val setSelectedPlatformUseCase: Lazy<SetSelectedPlatformUseCase>,
    val pickUpPlayerUseCase: Lazy<PickUpPlayerUseCase>
)
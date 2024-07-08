package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import dagger.Lazy

data class PickUpPlayerUseCases(
    val observeLatestPickedPlayersUseCase: Lazy<ObserveLatestPickedPlayersUseCase>,
    val getIsNotificationSoundEnabledUseCase: Lazy<GetIsNotificationSoundEnabledUseCase>,
    val getIsNotificationVibrateEnabledUseCase: Lazy<GetIsNotificationVibrateEnabledUseCase>,
    val getSelectedPlatformUseCase: Lazy<GetSelectedPlatformUseCase>,
    val setSelectedPlatformUseCase: Lazy<SetSelectedPlatformUseCase>,
    val pickUpPlayerUseCase: Lazy<PickUpPlayerUseCase>,
    val startCountDownTimerUseCase: Lazy<StartCountDownTimerUseCase>
)
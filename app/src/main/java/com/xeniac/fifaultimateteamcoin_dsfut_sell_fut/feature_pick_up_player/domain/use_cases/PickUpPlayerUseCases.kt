package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import dagger.Lazy

data class PickUpPlayerUseCases(
    val observeLatestPickedUpPlayersUseCase: Lazy<ObserveLatestPickedUpPlayersUseCase>,
    val getIsNotificationSoundEnabledUseCase: Lazy<GetIsNotificationSoundEnabledUseCase>,
    val getIsNotificationVibrateEnabledUseCase: Lazy<GetIsNotificationVibrateEnabledUseCase>,
    val getSelectedPlatformUseCase: Lazy<GetSelectedPlatformUseCase>,
    val storeSelectedPlatformUseCase: Lazy<StoreSelectedPlatformUseCase>,
    val pickUpPlayerUseCase: Lazy<PickUpPlayerUseCase>,
    val startCountDownTimerUseCase: Lazy<StartCountDownTimerUseCase>
)
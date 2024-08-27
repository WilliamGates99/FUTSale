package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.GetIsNotificationSoundEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.GetIsNotificationVibrateEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.GetSelectedPlatformUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.ObserveLatestPickedPlayersUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.PickUpPlayerUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.PickUpPlayerUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.StartCountDownTimerUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.StoreSelectedPlatformUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateMaxPrice
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateMinPrice
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidatePartnerId
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateSecretKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateTakeAfter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object PickUpPlayerModule {

    @Provides
    @ViewModelScoped
    fun provideValidatePartnerId(): ValidatePartnerId = ValidatePartnerId()

    @Provides
    @ViewModelScoped
    fun provideValidateSecretKey(): ValidateSecretKey = ValidateSecretKey()

    @Provides
    @ViewModelScoped
    fun provideValidateMinPrice(): ValidateMinPrice = ValidateMinPrice()

    @Provides
    @ViewModelScoped
    fun provideValidateMaxPrice(): ValidateMaxPrice = ValidateMaxPrice()

    @Provides
    @ViewModelScoped
    fun provideValidateTakeAfter(): ValidateTakeAfter = ValidateTakeAfter()

    @Provides
    @ViewModelScoped
    fun provideObserveLatestPickedPlayersUseCase(
        pickUpPlayerRepository: PickUpPlayerRepository
    ): ObserveLatestPickedPlayersUseCase = ObserveLatestPickedPlayersUseCase(pickUpPlayerRepository)

    @Provides
    @ViewModelScoped
    fun provideGetSelectedPlatformUseCase(
        preferencesRepository: PreferencesRepository
    ): GetSelectedPlatformUseCase = GetSelectedPlatformUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideGetIsNotificationSoundEnabledUseCase(
        preferencesRepository: PreferencesRepository
    ): GetIsNotificationSoundEnabledUseCase =
        GetIsNotificationSoundEnabledUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideGetIsNotificationVibrateEnabledUseCase(
        preferencesRepository: PreferencesRepository
    ): GetIsNotificationVibrateEnabledUseCase =
        GetIsNotificationVibrateEnabledUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreSelectedPlatformUseCase(
        preferencesRepository: PreferencesRepository
    ): StoreSelectedPlatformUseCase = StoreSelectedPlatformUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun providePickUpPlayerUseCase(
        preferencesRepository: PreferencesRepository,
        pickUpPlayerRepository: PickUpPlayerRepository,
        validatePartnerId: ValidatePartnerId,
        validateSecretKey: ValidateSecretKey,
        validateMinPrice: ValidateMinPrice,
        validateMaxPrice: ValidateMaxPrice,
        validateTakeAfter: ValidateTakeAfter
    ): PickUpPlayerUseCase = PickUpPlayerUseCase(
        preferencesRepository = preferencesRepository,
        pickUpPlayerRepository = pickUpPlayerRepository,
        validatePartnerId = validatePartnerId,
        validateSecretKey = validateSecretKey,
        validateMinPrice = validateMinPrice,
        validateMaxPrice = validateMaxPrice,
        validateTakeAfter = validateTakeAfter
    )

    @Provides
    @ViewModelScoped
    fun provideStartCountDownTimerUseCase(
        pickUpPlayerRepository: PickUpPlayerRepository
    ): StartCountDownTimerUseCase = StartCountDownTimerUseCase(pickUpPlayerRepository)

    @Provides
    @ViewModelScoped
    fun provideGetThreeLatestPlayersUseCase(
        observeLatestPickedPlayersUseCase: ObserveLatestPickedPlayersUseCase,
        getIsNotificationSoundEnabledUseCase: GetIsNotificationSoundEnabledUseCase,
        getIsNotificationVibrateEnabledUseCase: GetIsNotificationVibrateEnabledUseCase,
        getSelectedPlatformUseCase: GetSelectedPlatformUseCase,
        storeSelectedPlatformUseCase: StoreSelectedPlatformUseCase,
        pickUpPlayerUseCase: PickUpPlayerUseCase,
        startCountDownTimerUseCase: StartCountDownTimerUseCase
    ): PickUpPlayerUseCases = PickUpPlayerUseCases(
        { observeLatestPickedPlayersUseCase },
        { getIsNotificationSoundEnabledUseCase },
        { getIsNotificationVibrateEnabledUseCase },
        { getSelectedPlatformUseCase },
        { storeSelectedPlatformUseCase },
        { pickUpPlayerUseCase },
        { startCountDownTimerUseCase }
    )
}
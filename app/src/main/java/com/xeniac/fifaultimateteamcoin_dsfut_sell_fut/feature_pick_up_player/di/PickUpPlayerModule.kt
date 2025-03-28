package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.CountDownTimerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickedUpPlayersRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.GetIsNotificationSoundEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.GetIsNotificationVibrateEnabledUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.GetSelectedPlatformUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.ObserveLatestPickedUpPlayersUseCase
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
import javax.inject.Qualifier

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
    fun provideObserveLatestPickedUpPlayersUseCase(
        pickedUpPlayersRepository: PickedUpPlayersRepository
    ): ObserveLatestPickedUpPlayersUseCase = ObserveLatestPickedUpPlayersUseCase(
        pickedUpPlayersRepository
    )

    @Provides
    @ViewModelScoped
    fun provideGetSelectedPlatformUseCase(
        dsfutDataStoreRepository: DsfutDataStoreRepository
    ): GetSelectedPlatformUseCase = GetSelectedPlatformUseCase(dsfutDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideGetIsNotificationSoundEnabledUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): GetIsNotificationSoundEnabledUseCase = GetIsNotificationSoundEnabledUseCase(
        settingsDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideGetIsNotificationVibrateEnabledUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): GetIsNotificationVibrateEnabledUseCase = GetIsNotificationVibrateEnabledUseCase(
        settingsDataStoreRepository
    )

    @Provides
    @ViewModelScoped
    fun provideStoreSelectedPlatformUseCase(
        dsfutDataStoreRepository: DsfutDataStoreRepository
    ): StoreSelectedPlatformUseCase = StoreSelectedPlatformUseCase(dsfutDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun providePickUpPlayerUseCase(
        dsfutDataStoreRepository: DsfutDataStoreRepository,
        pickUpPlayerRepository: PickUpPlayerRepository,
        validatePartnerId: ValidatePartnerId,
        validateSecretKey: ValidateSecretKey,
        validateMinPrice: ValidateMinPrice,
        validateMaxPrice: ValidateMaxPrice,
        validateTakeAfter: ValidateTakeAfter
    ): PickUpPlayerUseCase = PickUpPlayerUseCase(
        dsfutDataStoreRepository = dsfutDataStoreRepository,
        pickUpPlayerRepository = pickUpPlayerRepository,
        validatePartnerId = validatePartnerId,
        validateSecretKey = validateSecretKey,
        validateMinPrice = validateMinPrice,
        validateMaxPrice = validateMaxPrice,
        validateTakeAfter = validateTakeAfter
    )

    @Provides
    @ViewModelScoped
    @PickUpPlayerQualifier
    fun provideStartCountDownTimerUseCase(
        countDownTimerRepository: CountDownTimerRepository
    ): StartCountDownTimerUseCase = StartCountDownTimerUseCase(countDownTimerRepository)

    @Provides
    @ViewModelScoped
    fun providePickUpPlayerUseCases(
        observeLatestPickedUpPlayersUseCase: ObserveLatestPickedUpPlayersUseCase,
        getIsNotificationSoundEnabledUseCase: GetIsNotificationSoundEnabledUseCase,
        getIsNotificationVibrateEnabledUseCase: GetIsNotificationVibrateEnabledUseCase,
        getSelectedPlatformUseCase: GetSelectedPlatformUseCase,
        storeSelectedPlatformUseCase: StoreSelectedPlatformUseCase,
        pickUpPlayerUseCase: PickUpPlayerUseCase,
        @PickUpPlayerQualifier startCountDownTimerUseCase: StartCountDownTimerUseCase
    ): PickUpPlayerUseCases = PickUpPlayerUseCases(
        { observeLatestPickedUpPlayersUseCase },
        { getIsNotificationSoundEnabledUseCase },
        { getIsNotificationVibrateEnabledUseCase },
        { getSelectedPlatformUseCase },
        { storeSelectedPlatformUseCase },
        { pickUpPlayerUseCase },
        { startCountDownTimerUseCase }
    )
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PickUpPlayerQualifier
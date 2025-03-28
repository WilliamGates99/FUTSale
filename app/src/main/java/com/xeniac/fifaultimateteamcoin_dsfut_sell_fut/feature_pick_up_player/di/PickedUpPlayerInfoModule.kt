package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.CountDownTimerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickedUpPlayersRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.ObservePickedUpPlayerUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.PickedUpPlayerInfoUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.StartCountDownTimerUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object PickedUpPlayerInfoModule {

    @Provides
    @ViewModelScoped
    fun provideObservePickedUpPlayerUseCase(
        pickedUpPlayersRepository: PickedUpPlayersRepository
    ): ObservePickedUpPlayerUseCase = ObservePickedUpPlayerUseCase(pickedUpPlayersRepository)

    @Provides
    @ViewModelScoped
    fun provideStartCountDownTimerUseCase(
        countDownTimerRepository: CountDownTimerRepository
    ): StartCountDownTimerUseCase = StartCountDownTimerUseCase(countDownTimerRepository)

    @Provides
    @ViewModelScoped
    fun providePickedUpPlayerInfoUseCases(
        observePickedUpPlayerUseCase: ObservePickedUpPlayerUseCase,
        startCountDownTimerUseCase: StartCountDownTimerUseCase
    ): PickedUpPlayerInfoUseCases = PickedUpPlayerInfoUseCases(
        { observePickedUpPlayerUseCase },
        { startCountDownTimerUseCase }
    )
}
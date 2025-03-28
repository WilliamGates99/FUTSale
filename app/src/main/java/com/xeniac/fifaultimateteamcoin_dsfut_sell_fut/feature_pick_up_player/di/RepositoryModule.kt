package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.CountDownTimerRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.PickUpPlayerRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.PickedUpPlayersRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.CountDownTimerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickedUpPlayersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindPickUpPlayerRepository(
        pickUpPlayerRepositoryImpl: PickUpPlayerRepositoryImpl
    ): PickUpPlayerRepository

    @Binds
    @ViewModelScoped
    abstract fun bindPickedUpPlayersRepository(
        pickedUpPlayersRepositoryImpl: PickedUpPlayersRepositoryImpl
    ): PickedUpPlayersRepository

    @Binds
    @ViewModelScoped
    abstract fun bindCountDownTimerRepository(
        countDownTimerRepositoryImpl: CountDownTimerRepositoryImpl
    ): CountDownTimerRepository
}
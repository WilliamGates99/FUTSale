package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.FakeCountDownTimerRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.FakePickUpPlayerRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.FakePickedUpPlayersRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.CountDownTimerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickedUpPlayersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class FakeRepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindPickUpPlayerRepository(
        fakePickUpPlayerRepositoryImpl: FakePickUpPlayerRepositoryImpl
    ): PickUpPlayerRepository

    @Binds
    @ViewModelScoped
    abstract fun bindPickedUpPlayersRepository(
        fakePickedUpPlayersRepositoryImpl: FakePickedUpPlayersRepositoryImpl
    ): PickedUpPlayersRepository

    @Binds
    @ViewModelScoped
    abstract fun bindCountDownTimerRepository(
        fakeCountDownTimerRepositoryImpl: FakeCountDownTimerRepositoryImpl
    ): CountDownTimerRepository
}
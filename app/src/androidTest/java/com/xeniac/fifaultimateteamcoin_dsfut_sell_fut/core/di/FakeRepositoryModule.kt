package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.data.repositories.FakeHistoryRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.repositories.HistoryRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories.FakeHomeRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.HomeRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.FakePickUpPlayerRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
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
    abstract fun bindHomeRepository(
        fakeHomeRepositoryImpl: FakeHomeRepositoryImpl
    ): HomeRepository

    @Binds
    @ViewModelScoped
    abstract fun bindPickUpPlayerRepository(
        fakePickUpPlayerRepositoryImp: FakePickUpPlayerRepositoryImpl
    ): PickUpPlayerRepository

    @Binds
    @ViewModelScoped
    abstract fun bindHistoryRepository(
        fakeHistoryRepositoryImp: FakeHistoryRepositoryImpl
    ): HistoryRepository
}
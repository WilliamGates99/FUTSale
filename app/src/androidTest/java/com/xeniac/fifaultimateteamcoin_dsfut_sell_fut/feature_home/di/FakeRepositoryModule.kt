package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories.FakeAppReviewRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories.FakeAppUpdateRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.AppReviewRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.AppUpdateRepository
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
    abstract fun bindAppUpdateRepository(
        fakeAppUpdateRepositoryImpl: FakeAppUpdateRepositoryImpl
    ): AppUpdateRepository

    @Binds
    @ViewModelScoped
    abstract fun bindHomeRepository(
        fakeAppReviewRepositoryImpl: FakeAppReviewRepositoryImpl
    ): AppReviewRepository
}
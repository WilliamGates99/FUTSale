package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories.AppReviewRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories.AppUpdateRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.AppReviewRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.AppUpdateRepository
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
    abstract fun bindAppUpdateRepository(
        appUpdateRepositoryImpl: AppUpdateRepositoryImpl
    ): AppUpdateRepository

    @Binds
    @ViewModelScoped
    abstract fun bindAppReviewRepository(
        appReviewRepositoryImpl: AppReviewRepositoryImpl
    ): AppReviewRepository
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case.GetIsOnBoardingCompletedUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelScoped::class)
class CoreModule {

    @Provides
    @ViewModelScoped
    fun provideGetIsOnBoardingCompletedUseCase(
        preferencesRepository: PreferencesRepository
    ): GetIsOnBoardingCompletedUseCase = GetIsOnBoardingCompletedUseCase(preferencesRepository)
}
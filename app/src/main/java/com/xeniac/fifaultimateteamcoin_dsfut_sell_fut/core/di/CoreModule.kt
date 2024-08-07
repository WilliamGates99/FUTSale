package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case.GetCurrentAppLocaleUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case.GetIsOnboardingCompletedUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case.MainUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal object CoreModule {

    @Provides
    @ViewModelScoped
    fun provideGetCurrentAppLocaleUseCase(
        preferencesRepository: PreferencesRepository
    ): GetCurrentAppLocaleUseCase = GetCurrentAppLocaleUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideGetIsOnboardingCompletedUseCase(
        preferencesRepository: PreferencesRepository
    ): GetIsOnboardingCompletedUseCase = GetIsOnboardingCompletedUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideMainUseCases(
        getCurrentAppLocaleUseCase: GetCurrentAppLocaleUseCase,
        getIsOnboardingCompletedUseCase: GetIsOnboardingCompletedUseCase
    ): MainUseCases = MainUseCases(
        { getCurrentAppLocaleUseCase },
        { getIsOnboardingCompletedUseCase }
    )
}
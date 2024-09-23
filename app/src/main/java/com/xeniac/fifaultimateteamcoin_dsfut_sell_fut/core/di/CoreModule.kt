package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
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
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): GetCurrentAppLocaleUseCase = GetCurrentAppLocaleUseCase(settingsDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideGetIsOnboardingCompletedUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): GetIsOnboardingCompletedUseCase = GetIsOnboardingCompletedUseCase(
        settingsDataStoreRepository
    )

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
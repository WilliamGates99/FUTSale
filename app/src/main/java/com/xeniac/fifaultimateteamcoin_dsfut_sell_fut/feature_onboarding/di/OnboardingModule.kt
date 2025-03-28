package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case.CompleteOnboardingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal object OnboardingModule {

    @Provides
    @ViewModelScoped
    fun provideCompleteOnboardingUseCase(
        settingsDataStoreRepository: SettingsDataStoreRepository,
        dsfutDataStoreRepository: DsfutDataStoreRepository
    ): CompleteOnboardingUseCase = CompleteOnboardingUseCase(
        settingsDataStoreRepository,
        dsfutDataStoreRepository
    )
}
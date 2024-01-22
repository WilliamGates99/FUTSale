package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case.OnboardingUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case.SetIsOnboardingCompletedUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case.SetPartnerIdUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case.SetSecretKeyUseCase
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
    fun provideSetIsOnboardingCompletedUseCase(
        preferencesRepository: PreferencesRepository
    ): SetIsOnboardingCompletedUseCase = SetIsOnboardingCompletedUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideSetPartnerIdUseCase(
        preferencesRepository: PreferencesRepository
    ): SetPartnerIdUseCase = SetPartnerIdUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideSetSecretKeyUseCase(
        preferencesRepository: PreferencesRepository
    ): SetSecretKeyUseCase = SetSecretKeyUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideOnboardingUseCases(
        setPartnerIdUseCase: SetPartnerIdUseCase,
        setSecretKeyUseCase: SetSecretKeyUseCase,
        setIsOnboardingCompletedUseCase: SetIsOnboardingCompletedUseCase
    ): OnboardingUseCases = OnboardingUseCases(
        { setPartnerIdUseCase },
        { setSecretKeyUseCase },
        { setIsOnboardingCompletedUseCase }
    )
}
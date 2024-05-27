package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases.GetProfileUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases.ProfileUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases.UpdatePartnerIdUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases.UpdateSecretKeyUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation.ValidatePartnerId
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation.ValidateSecretKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ProfileModule {

    @Provides
    @ViewModelScoped
    fun provideValidatePartnerId(): ValidatePartnerId = ValidatePartnerId()

    @Provides
    @ViewModelScoped
    fun provideValidateSecretKey(): ValidateSecretKey = ValidateSecretKey()

    @Provides
    @ViewModelScoped
    fun provideGetProfileUseCase(
        preferencesRepository: PreferencesRepository
    ): GetProfileUseCase = GetProfileUseCase(preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideUpdatePartnerIdUseCase(
        preferencesRepository: PreferencesRepository,
        validatePartnerId: ValidatePartnerId
    ): UpdatePartnerIdUseCase = UpdatePartnerIdUseCase(
        preferencesRepository,
        validatePartnerId
    )

    @Provides
    @ViewModelScoped
    fun provideUpdateSecretKeyUseCase(
        preferencesRepository: PreferencesRepository,
        validateSecretKey: ValidateSecretKey
    ): UpdateSecretKeyUseCase = UpdateSecretKeyUseCase(
        preferencesRepository,
        validateSecretKey
    )

    @Provides
    @ViewModelScoped
    fun provideCompleteOnboardingUseCase(
        getProfileUseCase: GetProfileUseCase,
        updatePartnerIdUseCase: UpdatePartnerIdUseCase,
        updateSecretKeyUseCase: UpdateSecretKeyUseCase
    ): ProfileUseCases = ProfileUseCases(
        { getProfileUseCase },
        { updatePartnerIdUseCase },
        { updateSecretKeyUseCase }
    )
}
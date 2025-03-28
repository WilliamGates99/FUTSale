package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.di

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases.GetPartnerIdUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases.GetSecretKeyUseCase
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
    fun provideGetPartnerIdUseCase(
        dsfutDataStoreRepository: DsfutDataStoreRepository
    ): GetPartnerIdUseCase = GetPartnerIdUseCase(dsfutDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideGetSecretKeyUseCase(
        dsfutDataStoreRepository: DsfutDataStoreRepository
    ): GetSecretKeyUseCase = GetSecretKeyUseCase(dsfutDataStoreRepository)

    @Provides
    @ViewModelScoped
    fun provideUpdatePartnerIdUseCase(
        dsfutDataStoreRepository: DsfutDataStoreRepository,
        validatePartnerId: ValidatePartnerId
    ): UpdatePartnerIdUseCase = UpdatePartnerIdUseCase(
        dsfutDataStoreRepository,
        validatePartnerId
    )

    @Provides
    @ViewModelScoped
    fun provideUpdateSecretKeyUseCase(
        dsfutDataStoreRepository: DsfutDataStoreRepository,
        validateSecretKey: ValidateSecretKey
    ): UpdateSecretKeyUseCase = UpdateSecretKeyUseCase(
        dsfutDataStoreRepository,
        validateSecretKey
    )

    @Provides
    @ViewModelScoped
    fun provideCompleteOnboardingUseCase(
        getPartnerIdUseCase: GetPartnerIdUseCase,
        getSecretKeyUseCase: GetSecretKeyUseCase,
        updatePartnerIdUseCase: UpdatePartnerIdUseCase,
        updateSecretKeyUseCase: UpdateSecretKeyUseCase
    ): ProfileUseCases = ProfileUseCases(
        { getPartnerIdUseCase },
        { getSecretKeyUseCase },
        { updatePartnerIdUseCase },
        { updateSecretKeyUseCase }
    )
}
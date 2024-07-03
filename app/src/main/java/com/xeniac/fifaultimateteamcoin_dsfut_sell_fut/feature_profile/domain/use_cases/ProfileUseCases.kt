package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import dagger.Lazy

data class ProfileUseCases(
    val getProfileUseCase: Lazy<GetProfileUseCase>,
    val updatePartnerIdUseCase: Lazy<UpdatePartnerIdUseCase>,
    val updateSecretKeyUseCase: Lazy<UpdateSecretKeyUseCase>
)
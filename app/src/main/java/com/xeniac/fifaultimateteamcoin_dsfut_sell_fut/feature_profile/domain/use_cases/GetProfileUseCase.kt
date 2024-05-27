package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.states.ProfileState

class GetProfileUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): ProfileState = ProfileState(
        partnerId = preferencesRepository.getPartnerId() ?: "",
        secretKey = preferencesRepository.getSecretKey() ?: ""
    )
}
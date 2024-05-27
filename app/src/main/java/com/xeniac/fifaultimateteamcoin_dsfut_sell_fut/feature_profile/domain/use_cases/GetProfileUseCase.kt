package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.states.ProfileState

class GetProfileUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): ProfileState {
        val partnerId = preferencesRepository.getPartnerId()
        val secretKey = preferencesRepository.getSecretKey()

        return ProfileState(
            partnerId = partnerId ?: "",
            secretKey = secretKey ?: "",
            isPartnerIdSaved = partnerId != null,
            isSecretKeySaved = secretKey != null
        )
    }
}
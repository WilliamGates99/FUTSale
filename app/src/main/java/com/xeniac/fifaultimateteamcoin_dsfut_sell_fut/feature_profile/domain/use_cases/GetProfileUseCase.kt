package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.states.ProfileState
import kotlinx.coroutines.flow.first

class GetProfileUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): ProfileState {
        val partnerId = preferencesRepository.getPartnerId().first()
        val secretKey = preferencesRepository.getSecretKey().first()

        return ProfileState(
            partnerId = partnerId ?: "",
            secretKey = secretKey ?: "",
            isPartnerIdSaved = partnerId != null,
            isSecretKeySaved = secretKey != null
        )
    }
}
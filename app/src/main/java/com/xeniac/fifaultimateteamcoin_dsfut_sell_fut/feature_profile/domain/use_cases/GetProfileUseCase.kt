package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.states.ProfileState
import kotlinx.coroutines.flow.first

class GetProfileUseCase(
    private val dsfutDataStoreRepository: DsfutDataStoreRepository
) {
    suspend operator fun invoke(): ProfileState {
        val partnerId = dsfutDataStoreRepository.getPartnerId().first()
        val secretKey = dsfutDataStoreRepository.getSecretKey().first()

        // TODO: UPDATE RETURN TYPE
//        return ProfileState(
//            partnerId = partnerId ?: "",
//            secretKey = secretKey ?: "",
//            isPartnerIdSaved = partnerId != null,
//            isSecretKeySaved = secretKey != null
//        )
        return ProfileState()
    }
}
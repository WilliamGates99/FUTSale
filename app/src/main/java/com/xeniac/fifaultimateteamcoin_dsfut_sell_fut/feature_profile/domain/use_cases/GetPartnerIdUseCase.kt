package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetPartnerIdUseCase(
    private val dsfutDataStoreRepository: DsfutDataStoreRepository
) {
    operator fun invoke(): Flow<String?> = dsfutDataStoreRepository.getPartnerId()
}
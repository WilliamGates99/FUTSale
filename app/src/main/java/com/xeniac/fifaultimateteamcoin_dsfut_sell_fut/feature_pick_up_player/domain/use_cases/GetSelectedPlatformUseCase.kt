package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetSelectedPlatformUseCase(
    private val dsfutDataStoreRepository: DsfutDataStoreRepository
) {
    operator fun invoke(): Flow<Platform> = dsfutDataStoreRepository.getSelectedPlatform()
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.HomeRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.IsUpdateDownloaded
import kotlinx.coroutines.flow.Flow

class CheckIsFlexibleUpdateStalledUseCase(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Flow<IsUpdateDownloaded> = homeRepository.checkIsFlexibleUpdateStalled()
}
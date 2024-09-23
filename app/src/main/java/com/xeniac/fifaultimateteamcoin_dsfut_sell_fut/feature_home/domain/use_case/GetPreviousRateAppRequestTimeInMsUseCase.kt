package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.MiscellaneousDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreviousRateAppRequestTimeInMs
import kotlinx.coroutines.flow.Flow

class GetPreviousRateAppRequestTimeInMsUseCase(
    private val miscellaneousDataStoreRepository: MiscellaneousDataStoreRepository
) {
    operator fun invoke(): Flow<PreviousRateAppRequestTimeInMs?> = miscellaneousDataStoreRepository
        .getPreviousRateAppRequestTimeInMs()
}
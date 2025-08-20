package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.PreviousRateAppRequestDateTime
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.MiscellaneousDataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetPreviousRateAppRequestDateTimeUseCase(
    private val miscellaneousDataStoreRepository: MiscellaneousDataStoreRepository
) {
    operator fun invoke(): Flow<PreviousRateAppRequestDateTime?> = miscellaneousDataStoreRepository
        .getPreviousRateAppRequestDateTime()
}
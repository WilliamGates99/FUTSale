package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreviousRateAppRequestTimeInMs
import kotlinx.coroutines.flow.Flow

class GetPreviousRateAppRequestTimeInMsUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<PreviousRateAppRequestTimeInMs?> =
        preferencesRepository.getPreviousRateAppRequestTimeInMs()
}
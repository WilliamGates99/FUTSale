package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreviousRateAppRequestTimeInMs

class GetPreviousRateAppRequestTimeInMsUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): PreviousRateAppRequestTimeInMs? =
        preferencesRepository.getPreviousRateAppRequestTimeInMs()
}
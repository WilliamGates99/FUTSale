package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetSelectedRateAppOptionUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<RateAppOption> = preferencesRepository.getSelectedRateAppOption()
}
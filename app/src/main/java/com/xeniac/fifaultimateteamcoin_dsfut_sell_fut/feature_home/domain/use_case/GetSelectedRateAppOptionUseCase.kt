package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository

class GetSelectedRateAppOptionUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): RateAppOption = preferencesRepository.getSelectedRateAppOption()
}
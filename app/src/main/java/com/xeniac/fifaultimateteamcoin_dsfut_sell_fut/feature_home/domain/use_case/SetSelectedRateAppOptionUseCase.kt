package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toRateAppOptionDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository

class SetSelectedRateAppOptionUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(rateAppOption: RateAppOption) = try {
        preferencesRepository.setSelectedRateAppOption(
            rateAppOptionDto = rateAppOption.toRateAppOptionDto()
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
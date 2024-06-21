package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository

class SetPreviousRateAppRequestTimeInMsUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke() = try {
        preferencesRepository.setPreviousRateAppRequestTimeInMs()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository

class StorePreviousRateAppRequestTimeInMsUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke() = try {
        preferencesRepository.storePreviousRateAppRequestTimeInMs()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
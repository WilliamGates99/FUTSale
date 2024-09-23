package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.MiscellaneousDataStoreRepository

class StorePreviousRateAppRequestTimeInMsUseCase(
    private val miscellaneousDataStoreRepository: MiscellaneousDataStoreRepository
) {
    suspend operator fun invoke() = try {
        miscellaneousDataStoreRepository.storePreviousRateAppRequestTimeInMs()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
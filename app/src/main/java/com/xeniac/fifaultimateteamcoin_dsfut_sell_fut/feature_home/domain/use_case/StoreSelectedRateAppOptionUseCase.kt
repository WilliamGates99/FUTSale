package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.MiscellaneousDataStoreRepository

class StoreSelectedRateAppOptionUseCase(
    private val miscellaneousDataStoreRepository: MiscellaneousDataStoreRepository
) {
    suspend operator fun invoke(rateAppOption: RateAppOption) = try {
        miscellaneousDataStoreRepository.storeSelectedRateAppOption(rateAppOption)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
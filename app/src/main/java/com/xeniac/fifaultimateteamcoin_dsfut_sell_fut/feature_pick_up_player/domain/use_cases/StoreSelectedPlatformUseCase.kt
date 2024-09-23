package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PlatformError

class StoreSelectedPlatformUseCase(
    private val dsfutDataStoreRepository: DsfutDataStoreRepository
) {
    suspend operator fun invoke(platform: Platform): Result<Unit, PlatformError> = try {
        dsfutDataStoreRepository.storeSelectedPlatform(platform)
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(PlatformError.SomethingWentWrong)
    }
}
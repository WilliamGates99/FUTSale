package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.errors.StoreSelectedPlatformError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StoreSelectedPlatformUseCase(
    private val dsfutDataStoreRepository: DsfutDataStoreRepository
) {
    operator fun invoke(
        platform: Platform
    ): Flow<Result<Unit, StoreSelectedPlatformError>> = flow {
        return@flow try {
            dsfutDataStoreRepository.storeSelectedPlatform(platform)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(StoreSelectedPlatformError.SomethingWentWrong))
        }
    }
}
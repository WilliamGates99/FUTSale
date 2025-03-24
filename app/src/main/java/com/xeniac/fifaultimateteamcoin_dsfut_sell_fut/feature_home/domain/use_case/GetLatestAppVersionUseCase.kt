package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.models.LatestAppUpdateInfo
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.AppUpdateRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.utils.GetLatestAppVersionError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLatestAppVersionUseCase(
    private val appUpdateRepository: AppUpdateRepository
) {
    operator fun invoke(): Flow<Result<LatestAppUpdateInfo?, GetLatestAppVersionError>> = flow {
        return@flow emit(appUpdateRepository.getLatestAppVersion())
    }
}
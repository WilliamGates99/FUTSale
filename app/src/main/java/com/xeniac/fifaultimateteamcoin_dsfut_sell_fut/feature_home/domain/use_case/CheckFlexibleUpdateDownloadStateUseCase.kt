package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.AppUpdateRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.IsUpdateDownloaded
import kotlinx.coroutines.flow.Flow

class CheckFlexibleUpdateDownloadStateUseCase(
    private val appUpdateRepository: AppUpdateRepository
) {
    operator fun invoke(): Flow<IsUpdateDownloaded> =
        appUpdateRepository.checkFlexibleUpdateDownloadState()
}
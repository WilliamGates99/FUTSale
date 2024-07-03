package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.repositories.HomeRepository
import kotlinx.coroutines.flow.Flow

class CheckForAppUpdatesUseCase(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Flow<AppUpdateInfo?> = homeRepository.checkForAppUpdates()
}
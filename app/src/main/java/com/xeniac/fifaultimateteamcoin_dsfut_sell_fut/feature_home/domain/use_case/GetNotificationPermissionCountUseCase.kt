package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationPermissionCountUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<Int> = preferencesRepository.getNotificationPermissionCount()
}
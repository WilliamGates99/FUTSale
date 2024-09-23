package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetIsNotificationVibrateEnabledUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    operator fun invoke(): Flow<Boolean> = settingsDataStoreRepository
        .isNotificationVibrateEnabled()
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetIsNotificationVibrateEnabledUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    operator fun invoke(): Flow<Boolean> = settingsDataStoreRepository
        .isNotificationVibrateEnabled()
}

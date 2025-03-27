package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.utils.StoreNotificationSoundError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StoreIsNotificationSoundEnabledUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    operator fun invoke(
        isEnabled: Boolean
    ): Flow<Result<Unit, StoreNotificationSoundError>> = flow {
        return@flow try {
            settingsDataStoreRepository.isNotificationSoundEnabled(isEnabled)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(StoreNotificationSoundError.SomethingWentWrong))
        }
    }
}
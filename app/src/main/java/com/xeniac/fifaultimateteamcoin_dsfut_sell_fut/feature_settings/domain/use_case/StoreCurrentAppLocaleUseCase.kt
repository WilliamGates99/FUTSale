package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.IsActivityRestartNeeded
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.utils.StoreAppLocaleError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StoreCurrentAppLocaleUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    operator fun invoke(
        newAppLocale: AppLocale
    ): Flow<Result<IsActivityRestartNeeded, StoreAppLocaleError>> = flow {
        return@flow try {
            emit(Result.Success(settingsDataStoreRepository.storeCurrentAppLocale(newAppLocale)))
        } catch (e: Exception) {
            emit(Result.Error(StoreAppLocaleError.SomethingWentWrong))
        }
    }
}
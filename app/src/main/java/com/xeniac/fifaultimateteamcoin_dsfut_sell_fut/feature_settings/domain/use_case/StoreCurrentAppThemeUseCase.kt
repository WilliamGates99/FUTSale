package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.utils.AppThemeError

class StoreCurrentAppThemeUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    suspend operator fun invoke(newAppTheme: AppTheme): Result<Unit, AppThemeError> = try {
        settingsDataStoreRepository.storeCurrentAppTheme(newAppTheme)
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(AppThemeError.SomethingWentWrong)
    }
}
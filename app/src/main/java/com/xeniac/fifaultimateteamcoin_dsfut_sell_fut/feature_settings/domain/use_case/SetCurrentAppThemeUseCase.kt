package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toAppThemeDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.utils.AppThemeError

class SetCurrentAppThemeUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(newAppTheme: AppTheme): Result<Unit, AppThemeError> = try {
        preferencesRepository.setCurrentAppTheme(appThemeDto = newAppTheme.toAppThemeDto())
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(AppThemeError.SomethingWentWrong)
    }
}
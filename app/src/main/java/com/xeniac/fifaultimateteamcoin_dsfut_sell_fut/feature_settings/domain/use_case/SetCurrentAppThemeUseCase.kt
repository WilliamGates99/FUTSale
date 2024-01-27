package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toAppThemeDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Resource
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.UiText

class SetCurrentAppThemeUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(newAppTheme: AppTheme): Resource<Nothing> = try {
        preferencesRepository.setCurrentAppTheme(appThemeDto = newAppTheme.toAppThemeDto())
        Resource.Success()
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.Error(UiText.StringResource(R.string.error_something_went_wrong))
    }
}
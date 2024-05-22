package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toAppThemeDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Resource
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.UiText

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
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toAppThemeDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppTheme

class SetCurrentAppThemeUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(index: Int) {
        val newAppTheme = AppTheme.entries.find { it.index == index } ?: AppTheme.DEFAULT
        preferencesRepository.setCurrentAppTheme(appThemeDto = newAppTheme.toAppThemeDto())
    }
}
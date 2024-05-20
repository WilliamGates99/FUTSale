package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme

class GetCurrentAppThemeUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): AppTheme = preferencesRepository.getCurrentAppTheme()
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentAppThemeUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<AppTheme> = preferencesRepository.getCurrentAppTheme()
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppLocale

class GetCurrentAppLocaleUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): AppLocale = preferencesRepository.getCurrentAppLocale()
}
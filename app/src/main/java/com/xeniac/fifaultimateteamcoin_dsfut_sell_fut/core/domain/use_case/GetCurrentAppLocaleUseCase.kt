package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale

class GetCurrentAppLocaleUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): AppLocale = preferencesRepository.getCurrentAppLocale()
}
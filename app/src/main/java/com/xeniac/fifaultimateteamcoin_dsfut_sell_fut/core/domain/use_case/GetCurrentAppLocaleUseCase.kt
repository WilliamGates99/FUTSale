package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository

class GetCurrentAppLocaleUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): AppLocale = preferencesRepository.getCurrentAppLocale()
}
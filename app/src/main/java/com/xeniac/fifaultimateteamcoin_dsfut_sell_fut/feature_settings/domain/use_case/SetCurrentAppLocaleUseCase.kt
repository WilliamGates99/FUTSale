package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toAppLocaleDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppLocale

class SetCurrentAppLocaleUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(index: Int) {
        val newAppLocale = AppLocale.entries.find { it.index == index } ?: AppLocale.DEFAULT
        preferencesRepository.setCurrentAppLocale(appLocaleDto = newAppLocale.toAppLocaleDto())
    }
}
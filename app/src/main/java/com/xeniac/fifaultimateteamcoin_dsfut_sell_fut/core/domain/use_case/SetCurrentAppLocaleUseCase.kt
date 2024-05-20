package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toAppLocaleDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.IsActivityRestartNeeded
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale

class SetCurrentAppLocaleUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(newAppLocale: AppLocale): IsActivityRestartNeeded =
        preferencesRepository.setCurrentAppLocale(appLocaleDto = newAppLocale.toAppLocaleDto())
}
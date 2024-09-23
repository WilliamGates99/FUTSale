package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.IsActivityRestartNeeded
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository

class StoreCurrentAppLocaleUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
) {
    suspend operator fun invoke(
        newAppLocale: AppLocale
    ): IsActivityRestartNeeded = settingsDataStoreRepository.storeCurrentAppLocale(newAppLocale)
}
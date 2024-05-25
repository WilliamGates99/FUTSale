package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.states.SettingsState

class GetCurrentSettingsUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(): SettingsState = SettingsState(
        appLocale = preferencesRepository.getCurrentAppLocale(),
        appTheme = preferencesRepository.getCurrentAppTheme(),
        isNotificationSoundEnabled = preferencesRepository.isNotificationSoundEnabled(),
        isNotificationVibrateEnabled = preferencesRepository.isNotificationVibrateEnabled()
    )
}
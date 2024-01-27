package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppTheme

sealed class SettingsEvent {
    data object GetCurrentAppTheme : SettingsEvent()
    data object GetCurrentAppLocale : SettingsEvent()
    data object GetIsNotificationSoundActive : SettingsEvent()
    data object GetIsNotificationVibrateActive : SettingsEvent()

    data class SetCurrentAppTheme(val newAppTheme: AppTheme) : SettingsEvent()
    data class SetCurrentAppLocale(val newAppLocale: AppLocale) : SettingsEvent()
    data class SetNotificationSoundSwitch(val isEnabled: Boolean) : SettingsEvent()
    data class SetNotificationVibrateSwitch(val isEnabled: Boolean) : SettingsEvent()
}
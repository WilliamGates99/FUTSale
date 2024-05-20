package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme

sealed class SettingsEvent {
    data object GetCurrentAppTheme : SettingsEvent()
    data object GetCurrentAppLocale : SettingsEvent()
    data object GetIsNotificationSoundEnabled : SettingsEvent()
    data object GetIsNotificationVibrateEnabled : SettingsEvent()

    data class SetCurrentAppTheme(val newAppTheme: AppTheme) : SettingsEvent()
    data class SetCurrentAppLocale(val newAppLocale: AppLocale) : SettingsEvent()
    data class SetNotificationSoundSwitch(val isEnabled: Boolean) : SettingsEvent()
    data class SetNotificationVibrateSwitch(val isEnabled: Boolean) : SettingsEvent()
}
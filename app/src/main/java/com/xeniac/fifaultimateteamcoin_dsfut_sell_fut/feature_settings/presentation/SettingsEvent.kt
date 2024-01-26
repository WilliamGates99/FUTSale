package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation

sealed class SettingsEvent {
    data object GetCurrentAppTheme : SettingsEvent()
    data object GetCurrentAppLocale : SettingsEvent()
    data object GetIsNotificationSoundActive : SettingsEvent()
    data object GetIsNotificationVibrateActive : SettingsEvent()

    data class SetCurrentAppTheme(val index: Int) : SettingsEvent()
    data class SetCurrentAppLocale(val index: Int) : SettingsEvent()
    data class SetNotificationSoundSwitch(val isEnabled: Boolean) : SettingsEvent()
    data class SetNotificationVibrateSwitch(val isEnabled: Boolean) : SettingsEvent()
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme

sealed interface SettingsAction {
    data class SetCurrentAppLocale(val newAppLocale: AppLocale) : SettingsAction
    data class SetCurrentAppTheme(val newAppTheme: AppTheme) : SettingsAction
    data class SetNotificationSoundSwitch(val isEnabled: Boolean) : SettingsAction
    data class SetNotificationVibrateSwitch(val isEnabled: Boolean) : SettingsAction
}
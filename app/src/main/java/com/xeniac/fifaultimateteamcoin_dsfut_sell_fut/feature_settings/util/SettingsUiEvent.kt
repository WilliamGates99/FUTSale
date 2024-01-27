package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.util

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Event

sealed class SettingsUiEvent : Event() {
    data class UpdateAppTheme(val newAppTheme: AppTheme) : SettingsUiEvent()
    data object RestartActivity : SettingsUiEvent()
}
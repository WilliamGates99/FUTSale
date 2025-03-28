package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.events

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event

sealed class SettingsUiEvent : Event() {
    data class UpdateAppTheme(val newAppTheme: AppTheme) : SettingsUiEvent()
    data object RestartActivity : SettingsUiEvent()
}
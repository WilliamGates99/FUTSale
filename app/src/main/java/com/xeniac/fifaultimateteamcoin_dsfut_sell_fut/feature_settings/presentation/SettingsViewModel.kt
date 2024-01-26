package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation

import androidx.lifecycle.ViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.GetCurrentAppTheme -> {

            }
            SettingsEvent.GetCurrentAppLocale -> {

            }
            SettingsEvent.GetIsNotificationSoundActive -> {

            }
            SettingsEvent.GetIsNotificationVibrateActive -> {

            }
            is SettingsEvent.SetCurrentAppTheme -> {

            }
            is SettingsEvent.SetCurrentAppLocale -> {

            }
            is SettingsEvent.SetNotificationSoundSwitch -> {

            }
            is SettingsEvent.SetNotificationVibrateSwitch -> {

            }
        }
    }
}
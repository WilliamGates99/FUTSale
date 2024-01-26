package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    private val _currentAppThemeText = MutableStateFlow<UiText?>(null)
    val currentAppThemeText = _currentAppThemeText.asStateFlow()

    private val _currentAppLocaleText = MutableStateFlow<UiText?>(null)
    val currentAppLocaleText = _currentAppLocaleText.asStateFlow()

    private val _isNotificationSoundActive = MutableStateFlow<Boolean?>(null)
    val isNotificationSoundActive = _isNotificationSoundActive.asStateFlow()

    private val _isNotificationVibrateActive = MutableStateFlow<Boolean?>(null)
    val isNotificationVibrateActive = _isNotificationVibrateActive.asStateFlow()

    init {
        onEvent(SettingsEvent.GetCurrentAppTheme)
        onEvent(SettingsEvent.GetCurrentAppLocale)
        onEvent(SettingsEvent.GetIsNotificationSoundActive)
        onEvent(SettingsEvent.GetIsNotificationVibrateActive)
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.GetCurrentAppTheme -> getCurrentAppTheme()
            SettingsEvent.GetCurrentAppLocale -> getCurrentAppLocale()
            SettingsEvent.GetIsNotificationSoundActive -> getIsNotificationSoundActive()
            SettingsEvent.GetIsNotificationVibrateActive -> getIsNotificationVibrateActive()
            is SettingsEvent.SetCurrentAppTheme -> {}
            is SettingsEvent.SetCurrentAppLocale -> {}
            is SettingsEvent.SetNotificationSoundSwitch -> {}
            is SettingsEvent.SetNotificationVibrateSwitch -> {}
        }
    }

    private fun getCurrentAppTheme() = viewModelScope.launch {
        _currentAppThemeText.value = settingsUseCases.getCurrentAppThemeUseCase.get()()
    }

    private fun getCurrentAppLocale() = viewModelScope.launch {
        _currentAppLocaleText.value = settingsUseCases.getCurrentAppLocaleUseCase.get()()
    }

    private fun getIsNotificationSoundActive() = viewModelScope.launch {
        _isNotificationSoundActive.value = settingsUseCases
            .getIsNotificationSoundEnabledUseCase.get()()
    }

    private fun getIsNotificationVibrateActive() = viewModelScope.launch {
        _isNotificationVibrateActive.value = settingsUseCases
            .getIsNotificationVibrateEnabledUseCase.get()()
    }
}
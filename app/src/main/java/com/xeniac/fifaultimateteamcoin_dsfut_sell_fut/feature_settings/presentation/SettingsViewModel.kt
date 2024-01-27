package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.model.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Resource
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SettingsUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.util.SettingsUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    val appTheme = savedStateHandle.getStateFlow<AppTheme?>(
        key = "appTheme",
        initialValue = null
    )

    val appLocale = savedStateHandle.getStateFlow<AppLocale?>(
        key = "appLocale",
        initialValue = null
    )

    val isNotificationSoundActive = savedStateHandle.getStateFlow<Boolean?>(
        key = "isNotificationSoundActive",
        initialValue = null
    )

    val isNotificationVibrateActive = savedStateHandle.getStateFlow<Boolean?>(
        key = "isNotificationVibrateActive",
        initialValue = null
    )

    private val _setAppThemeEventChannel = Channel<Event>()
    val setAppThemeEventChannel = _setAppThemeEventChannel.receiveAsFlow()

    private val _setAppLocaleEventChannel = Channel<Event>()
    val setAppLocaleEventChannel = _setAppLocaleEventChannel.receiveAsFlow()

    init {
        getCurrentAppTheme()
        getCurrentAppLocale()
        getIsNotificationSoundActive()
        getIsNotificationVibrateActive()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.GetCurrentAppTheme -> getCurrentAppTheme()
            SettingsEvent.GetCurrentAppLocale -> getCurrentAppLocale()
            SettingsEvent.GetIsNotificationSoundActive -> getIsNotificationSoundActive()
            SettingsEvent.GetIsNotificationVibrateActive -> getIsNotificationVibrateActive()
            is SettingsEvent.SetCurrentAppTheme -> setCurrentAppTheme(event.newAppTheme)
            is SettingsEvent.SetCurrentAppLocale -> setCurrentAppLocale(event.newAppLocale)
            is SettingsEvent.SetNotificationSoundSwitch -> setNotificationSoundSwitch(event.isEnabled)
            is SettingsEvent.SetNotificationVibrateSwitch -> setNotificationVibrateSwitch(event.isEnabled)
        }
    }

    private fun getCurrentAppTheme() = viewModelScope.launch {
        savedStateHandle["appTheme"] = settingsUseCases.getCurrentAppThemeUseCase.get()()
    }

    private fun getCurrentAppLocale() = viewModelScope.launch {
        savedStateHandle["appLocale"] = settingsUseCases.getCurrentAppLocaleUseCase.get()()
    }

    private fun getIsNotificationSoundActive() = viewModelScope.launch {
        savedStateHandle["isNotificationSoundActive"] = settingsUseCases
            .getIsNotificationSoundEnabledUseCase.get()()
    }

    private fun getIsNotificationVibrateActive() = viewModelScope.launch {
        savedStateHandle["isNotificationVibrateActive"] = settingsUseCases
            .getIsNotificationVibrateEnabledUseCase.get()()
    }

    private fun setCurrentAppTheme(newAppTheme: AppTheme) = viewModelScope.launch {
        when (val result = settingsUseCases.setCurrentAppThemeUseCase.get()(newAppTheme)) {
            is Resource.Success -> {
                _setAppThemeEventChannel.send(SettingsUiEvent.UpdateAppTheme(newAppTheme))
            }
            is Resource.Error -> {
                result.message?.let { message ->
                    _setAppThemeEventChannel.send(UiEvent.ShowSnackbar(message))
                }
            }
        }
    }

    private fun setCurrentAppLocale(newAppLocale: AppLocale) = viewModelScope.launch {
        when (settingsUseCases.setCurrentAppLocaleUseCase.get()(newAppLocale)) {
            true -> _setAppLocaleEventChannel.send(SettingsUiEvent.RestartActivity)
            false -> Unit
        }
    }

    private fun setNotificationSoundSwitch(isEnabled: Boolean) = viewModelScope.launch {
        settingsUseCases.setIsNotificationSoundEnabledUseCase.get()(isEnabled)
    }

    private fun setNotificationVibrateSwitch(isEnabled: Boolean) = viewModelScope.launch {
        settingsUseCases.setIsNotificationVibrateEnabledUseCase.get()(isEnabled)
    }
}
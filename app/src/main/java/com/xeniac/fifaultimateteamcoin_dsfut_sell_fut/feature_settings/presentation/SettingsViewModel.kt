package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.states.SettingsState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SettingsUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.utils.AppThemeError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.utils.NotificationSoundError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.utils.NotificationVibrateError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.util.SettingsUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val settingsState = savedStateHandle.getStateFlow(
        key = "settingsState",
        initialValue = SettingsState()
    )

    private val _setAppLocaleEventChannel = Channel<Event>()
    val setAppLocaleEventChannel = _setAppLocaleEventChannel.receiveAsFlow()

    private val _setAppThemeEventChannel = Channel<Event>()
    val setAppThemeEventChannel = _setAppThemeEventChannel.receiveAsFlow()

    private val _setNotificationSoundEventChannel = Channel<UiEvent>()
    val setNotificationSoundEventChannel = _setNotificationSoundEventChannel.receiveAsFlow()

    private val _setNotificationVibrateEventChannel = Channel<UiEvent>()
    val setNotificationVibrateEventChannel = _setNotificationVibrateEventChannel.receiveAsFlow()

    init {
        getCurrentAppLocale()
        getCurrentAppTheme()
        getIsNotificationSoundEnabled()
        getIsNotificationVibrateEnabled()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.GetCurrentAppLocale -> getCurrentAppLocale()
            SettingsEvent.GetCurrentAppTheme -> getCurrentAppTheme()
            SettingsEvent.GetIsNotificationSoundEnabled -> getIsNotificationSoundEnabled()
            SettingsEvent.GetIsNotificationVibrateEnabled -> getIsNotificationVibrateEnabled()
            is SettingsEvent.SetCurrentAppLocale -> setCurrentAppLocale(event.newAppLocale)
            is SettingsEvent.SetCurrentAppTheme -> setCurrentAppTheme(event.newAppTheme)
            is SettingsEvent.SetNotificationSoundSwitch -> setNotificationSoundSwitch(event.isEnabled)
            is SettingsEvent.SetNotificationVibrateSwitch -> setNotificationVibrateSwitch(event.isEnabled)
        }
    }

    private fun getCurrentAppLocale() = viewModelScope.launch {
        savedStateHandle["settingsState"] = settingsState.value.copy(
            appLocale = settingsUseCases.getCurrentAppLocaleUseCase.get()()
        )
    }

    private fun getCurrentAppTheme() = viewModelScope.launch {
        savedStateHandle["settingsState"] = settingsState.value.copy(
            appTheme = settingsUseCases.getCurrentAppThemeUseCase.get()()
        )
    }

    private fun getIsNotificationSoundEnabled() = viewModelScope.launch {
        savedStateHandle["settingsState"] = settingsState.value.copy(
            isNotificationSoundEnabled = settingsUseCases.getIsNotificationSoundEnabledUseCase.get()()
        )
    }

    private fun getIsNotificationVibrateEnabled() = viewModelScope.launch {
        savedStateHandle["settingsState"] = settingsState.value.copy(
            isNotificationVibrateEnabled = settingsUseCases.getIsNotificationVibrateEnabledUseCase.get()()
        )
    }

    private fun setCurrentAppLocale(newAppLocale: AppLocale) = viewModelScope.launch {
        val isActivityRestartNeeded = settingsUseCases.setCurrentAppLocaleUseCase.get()(
            newAppLocale = newAppLocale
        )

        when (isActivityRestartNeeded) {
            true -> _setAppLocaleEventChannel.send(SettingsUiEvent.RestartActivity)
            false -> Unit
        }
    }

    private fun setCurrentAppTheme(newAppTheme: AppTheme) = viewModelScope.launch {
        when (val result = settingsUseCases.setCurrentAppThemeUseCase.get()(newAppTheme)) {
            is Result.Success -> {
                _setAppThemeEventChannel.send(SettingsUiEvent.UpdateAppTheme(newAppTheme))
            }
            is Result.Error -> {
                when (result.error) {
                    is AppThemeError.SomethingWentWrong -> {
                        _setAppThemeEventChannel.send(
                            UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_something_went_wrong))
                        )
                    }
                }
            }
        }
    }

    private fun setNotificationSoundSwitch(isEnabled: Boolean) = viewModelScope.launch {
        when (val result = settingsUseCases.setIsNotificationSoundEnabledUseCase.get()(isEnabled)) {
            is Result.Success -> {
                savedStateHandle["settingsState"] = settingsState.value.copy(
                    isNotificationVibrateEnabled = isEnabled
                )
            }
            is Result.Error -> {
                when (result.error) {
                    NotificationSoundError.SomethingWentWrong -> {
                        _setNotificationSoundEventChannel.send(
                            UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_something_went_wrong))
                        )
                    }
                }
            }
        }
    }

    private fun setNotificationVibrateSwitch(isEnabled: Boolean) = viewModelScope.launch {
        when (val result = settingsUseCases
            .setIsNotificationVibrateEnabledUseCase.get()(isEnabled)) {
            is Result.Success -> {
                savedStateHandle["settingsState"] = settingsState.value.copy(
                    isNotificationVibrateEnabled = isEnabled
                )
            }
            is Result.Error -> {
                when (result.error) {
                    NotificationVibrateError.SomethingWentWrong -> {
                        _setNotificationVibrateEventChannel.send(
                            UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_something_went_wrong))
                        )
                    }
                }
            }
        }
    }
}
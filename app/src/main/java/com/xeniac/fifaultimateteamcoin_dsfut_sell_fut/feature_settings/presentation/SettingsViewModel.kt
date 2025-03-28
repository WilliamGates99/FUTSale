package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SettingsUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.events.SettingsAction
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.events.SettingsUiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.states.SettingsState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.util.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState = combine(
        flow = _settingsState,
        flow2 = settingsUseCases.getCurrentAppLocaleUseCase.get()(),
        flow3 = settingsUseCases.getCurrentAppThemeUseCase.get()(),
        flow4 = settingsUseCases.getIsNotificationSoundEnabledUseCase.get()(),
        flow5 = settingsUseCases.getIsNotificationVibrateEnabledUseCase.get()()
    ) { settingsState, appLocale, appTheme, isNotificationSoundEnabled, isNotificationVibrateEnabled ->
        _settingsState.update {
            settingsState.copy(
                currentAppLocale = appLocale,
                currentAppTheme = appTheme,
                isNotificationSoundEnabled = isNotificationSoundEnabled,
                isNotificationVibrateEnabled = isNotificationVibrateEnabled
            )
        }
        _settingsState.value
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
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

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.ShowLocaleBottomSheet -> showLocaleBottomSheet()
            SettingsAction.DismissLocaleBottomSheet -> dismissLocaleBottomSheet()
            SettingsAction.ShowThemeBottomSheet -> showThemeBottomSheet()
            SettingsAction.DismissThemeBottomSheet -> dismissThemeBottomSheet()
            is SettingsAction.SetCurrentAppLocale -> setCurrentAppLocale(action.newAppLocale)
            is SettingsAction.SetCurrentAppTheme -> setCurrentAppTheme(action.newAppTheme)
            is SettingsAction.SetNotificationSoundSwitch -> setNotificationSoundSwitch(action.isEnabled)
            is SettingsAction.SetNotificationVibrateSwitch -> setNotificationVibrateSwitch(action.isEnabled)
        }
    }

    private fun showLocaleBottomSheet() = viewModelScope.launch {
        _settingsState.update { state ->
            state.copy(isLocaleBottomSheetVisible = true)
        }
    }

    private fun dismissLocaleBottomSheet() = viewModelScope.launch {
        _settingsState.update { state ->
            state.copy(isLocaleBottomSheetVisible = false)
        }
    }

    private fun showThemeBottomSheet() = viewModelScope.launch {
        _settingsState.update { state ->
            state.copy(isThemeBottomSheetVisible = true)
        }
    }

    private fun dismissThemeBottomSheet() = viewModelScope.launch {
        _settingsState.update { state ->
            state.copy(isThemeBottomSheetVisible = false)
        }
    }

    private fun setCurrentAppLocale(newAppLocale: AppLocale) {
        val shouldUpdateAppLocale = newAppLocale != _settingsState.value.currentAppLocale
        if (shouldUpdateAppLocale) {
            settingsUseCases.storeCurrentAppLocaleUseCase.get()(
                newAppLocale = newAppLocale
            ).onEach { result ->
                when (result) {
                    is Result.Success -> result.data.let { isActivityRestartNeeded ->
                        _settingsState.update { state ->
                            state.copy(currentAppLocale = newAppLocale)
                        }

                        if (isActivityRestartNeeded) {
                            _setAppLocaleEventChannel.send(SettingsUiEvent.RestartActivity)
                        }
                    }
                    is Result.Error -> {
                        _setAppLocaleEventChannel.send(
                            UiEvent.ShowShortSnackbar(result.error.asUiText())
                        )
                    }
                }
            }.launchIn(scope = viewModelScope)
        }
    }

    private fun setCurrentAppTheme(newAppTheme: AppTheme) {
        val shouldUpdateAppTheme = newAppTheme != _settingsState.value.currentAppTheme
        if (shouldUpdateAppTheme) {
            settingsUseCases.storeCurrentAppThemeUseCase.get()(
                newAppTheme = newAppTheme
            ).onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _setAppThemeEventChannel.send(SettingsUiEvent.UpdateAppTheme(newAppTheme))
                    }
                    is Result.Error -> {
                        _setAppThemeEventChannel.send(
                            UiEvent.ShowShortSnackbar(result.error.asUiText())
                        )
                    }
                }
            }.launchIn(scope = viewModelScope)
        }
    }

    private fun setNotificationSoundSwitch(isEnabled: Boolean) {
        settingsUseCases.storeIsNotificationSoundEnabledUseCase.get()(
            isEnabled = isEnabled
        ).onEach { result ->
            when (result) {
                is Result.Success -> Unit
                is Result.Error -> {
                    _setNotificationSoundEventChannel.send(
                        UiEvent.ShowShortSnackbar(result.error.asUiText())
                    )
                }
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun setNotificationVibrateSwitch(isEnabled: Boolean) {
        settingsUseCases.storeIsNotificationVibrateEnabledUseCase.get()(
            isEnabled = isEnabled
        ).onEach { result ->
            when (result) {
                is Result.Success -> Unit
                is Result.Error -> {
                    _setNotificationVibrateEventChannel.send(
                        UiEvent.ShowShortSnackbar(result.error.asUiText())
                    )
                }
            }
        }.launchIn(scope = viewModelScope)
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case.SettingsUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.utils.AppThemeError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.utils.NotificationSoundError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.utils.NotificationVibrateError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.states.SettingsState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.presentation.util.SettingsUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
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
        flow2 = settingsUseCases.getCurrentAppThemeUseCase.get()(),
        flow3 = settingsUseCases.getIsNotificationSoundEnabledUseCase.get()(),
        flow4 = settingsUseCases.getIsNotificationVibrateEnabledUseCase.get()()
    ) { settingsState, appTheme, isNotificationSoundEnabled, isNotificationVibrateEnabled ->
        settingsState.copy(
            currentAppTheme = appTheme,
            isNotificationSoundEnabled = isNotificationSoundEnabled,
            isNotificationVibrateEnabled = isNotificationVibrateEnabled
        )
    }.onStart {
        _settingsState.update { state ->
            state.copy(
                currentAppLocale = settingsUseCases.getCurrentAppLocaleUseCase.get()()
            )
        }
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

    private fun setCurrentAppLocale(newAppLocale: AppLocale) = viewModelScope.launch {
        val shouldUpdateAppLocale = newAppLocale != settingsState.value.currentAppLocale
        if (shouldUpdateAppLocale) {
            val isActivityRestartNeeded = settingsUseCases.storeCurrentAppLocaleUseCase.get()(
                newAppLocale = newAppLocale
            )

            _settingsState.update { state ->
                state.copy(currentAppLocale = newAppLocale)
            }

            when (isActivityRestartNeeded) {
                true -> _setAppLocaleEventChannel.send(SettingsUiEvent.RestartActivity)
                false -> Unit
            }
        }
    }

    private fun setCurrentAppTheme(newAppTheme: AppTheme) = viewModelScope.launch {
        val shouldUpdateAppTheme = newAppTheme != settingsState.value.currentAppTheme
        if (shouldUpdateAppTheme) {
            when (val result = settingsUseCases.storeCurrentAppThemeUseCase.get()(newAppTheme)) {
                is Result.Success -> {
                    _setAppThemeEventChannel.send(SettingsUiEvent.UpdateAppTheme(newAppTheme))
                }
                is Result.Error -> {
                    when (result.error) {
                        is AppThemeError.SomethingWentWrong -> {
                            _setAppThemeEventChannel.send(
                                UiEvent.ShowShortSnackbar(UiText.StringResource(R.string.error_something_went_wrong))
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setNotificationSoundSwitch(isEnabled: Boolean) = viewModelScope.launch {
        when (val result =
            settingsUseCases.storeIsNotificationSoundEnabledUseCase.get()(isEnabled)) {
            is Result.Success -> Unit
            is Result.Error -> {
                when (result.error) {
                    NotificationSoundError.SomethingWentWrong -> {
                        _setNotificationSoundEventChannel.send(
                            UiEvent.ShowShortSnackbar(UiText.StringResource(R.string.error_something_went_wrong))
                        )
                    }
                }
            }
        }
    }

    private fun setNotificationVibrateSwitch(isEnabled: Boolean) = viewModelScope.launch {
        when (val result = settingsUseCases
            .storeIsNotificationVibrateEnabledUseCase.get()(isEnabled)) {
            is Result.Success -> Unit
            is Result.Error -> {
                when (result.error) {
                    NotificationVibrateError.SomethingWentWrong -> {
                        _setNotificationVibrateEventChannel.send(
                            UiEvent.ShowShortSnackbar(UiText.StringResource(R.string.error_something_went_wrong))
                        )
                    }
                }
            }
        }
    }
}
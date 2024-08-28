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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mutex: Mutex = Mutex()

    private val _appTheme = settingsUseCases.getCurrentAppThemeUseCase.get()().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )
    private val _isNotificationSoundEnabled = settingsUseCases.getIsNotificationSoundEnabledUseCase
        .get()().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )
    private val _isNotificationVibrateEnabled =
        settingsUseCases.getIsNotificationVibrateEnabledUseCase.get()().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    private val _appLocale = savedStateHandle.getStateFlow<AppLocale?>(
        key = "appLocale",
        initialValue = settingsUseCases.getCurrentAppLocaleUseCase.get()()
    )

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState = combine(
        flow = _settingsState,
        flow2 = _appTheme,
        flow3 = _appLocale,
        flow4 = _isNotificationSoundEnabled,
        flow5 = _isNotificationVibrateEnabled
    ) { settingsState, appTheme, appLocale, isNotificationSoundEnabled, isNotificationVibrateEnabled ->
        settingsState.copy(
            currentAppTheme = appTheme,
            currentAppLocale = appLocale,
            isNotificationSoundEnabled = isNotificationSoundEnabled,
            isNotificationVibrateEnabled = isNotificationVibrateEnabled
        )
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

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SetCurrentAppLocale -> setCurrentAppLocale(event.newAppLocale)
            is SettingsEvent.SetCurrentAppTheme -> setCurrentAppTheme(event.newAppTheme)
            is SettingsEvent.SetNotificationSoundSwitch -> setNotificationSoundSwitch(event.isEnabled)
            is SettingsEvent.SetNotificationVibrateSwitch -> setNotificationVibrateSwitch(event.isEnabled)
        }
    }

    private fun setCurrentAppLocale(newAppLocale: AppLocale) = viewModelScope.launch {
        val shouldUpdateAppLocale = newAppLocale != settingsState.value.currentAppLocale
        if (shouldUpdateAppLocale) {
            val isActivityRestartNeeded = settingsUseCases.storeCurrentAppLocaleUseCase.get()(
                newAppLocale = newAppLocale
            )

            mutex.withLock {
                savedStateHandle["appLocale"] = newAppLocale
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
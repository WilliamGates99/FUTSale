package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.convertDigitsToEnglish
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.toEnglishDigits
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case.CompleteOnboardingUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.states.OnboardingState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.utils.asUiText
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboardingUseCase: Lazy<CompleteOnboardingUseCase>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = savedStateHandle.getMutableStateFlow(
        key = "onboardingState",
        initialValue = OnboardingState()
    )
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = _state.value
    )

    private val _completeOnboardingEventChannel = Channel<Event>()
    val completeOnboardingEventChannel = _completeOnboardingEventChannel.receiveAsFlow()

    fun onAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.PartnerIdChanged -> partnerIdChanged(action.newValue)
            is OnboardingAction.SecretKeyChanged -> secretKeyChanged(action.newValue)
            OnboardingAction.SaveUserData -> saveUserData()
        }
    }

    private fun partnerIdChanged(
        newValue: TextFieldValue
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                partnerIdState = it.partnerIdState.copy(
                    value = newValue.copy(text = newValue.text.toEnglishDigits()),
                    errorText = null
                )
            )
        }
    }

    private fun secretKeyChanged(
        newValue: TextFieldValue
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                secretKeyState = it.secretKeyState.copy(
                    value = newValue.copy(text = newValue.text.convertDigitsToEnglish()),
                    errorText = null
                )
            )
        }
    }

    private fun saveUserData() {
        completeOnboardingUseCase.get()(
            partnerId = _state.value.partnerIdState.value.text,
            secretKey = _state.value.secretKeyState.value.text
        ).onStart {
            _state.update {
                it.copy(isCompleteLoading = true)
            }
        }.onEach { result ->
            when (result) {
                is Result.Success -> {
                    _completeOnboardingEventChannel.send(OnboardingUiEvent.NavigateToHomeScreen)
                }
                is Result.Error -> {
                    _completeOnboardingEventChannel.send(
                        UiEvent.ShowShortSnackbar(result.error.asUiText())
                    )
                }
            }
        }.onCompletion {
            _state.update {
                it.copy(isCompleteLoading = false)
            }
        }.launchIn(scope = viewModelScope)
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.convertDigitsToEnglish
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.toEnglishDigits
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case.CompleteOnboardingUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.states.OnboardingState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.utils.OnboardingUiEvent
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboardingUseCase: Lazy<CompleteOnboardingUseCase>,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mutex: Mutex = Mutex()

    private val _onboardingState = savedStateHandle.getStateFlow(
        key = "onboardingState",
        initialValue = OnboardingState()
    )
    val onboardingState = _onboardingState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = OnboardingState()
    )

    private val _completeOnboardingEventChannel = Channel<Event>()
    val completeOnboardingEventChannel = _completeOnboardingEventChannel.receiveAsFlow()

    fun onAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.PartnerIdChanged -> partnerIdChanged(action.partnerId)
            is OnboardingAction.SecretKeyChanged -> secretKeyChanged(action.secretKey)
            OnboardingAction.SaveUserData -> saveUserData()
        }
    }

    private fun partnerIdChanged(partnerId: String) = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["onboardingState"] = _onboardingState.value.copy(
                partnerId = partnerId.toEnglishDigits(),
                partnerIdErrorText = null
            )
        }
    }

    private fun secretKeyChanged(secretKey: String) = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["onboardingState"] = _onboardingState.value.copy(
                secretKey = secretKey.convertDigitsToEnglish(),
                secretKeyErrorText = null
            )
        }
    }

    private fun saveUserData() {
        completeOnboardingUseCase.get()(
            partnerId = _onboardingState.value.partnerId,
            secretKey = _onboardingState.value.secretKey
        ).onStart {
            mutex.withLock {
                savedStateHandle["secretKeyState"] = _onboardingState.value.copy(
                    isCompleteLoading = true
                )
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
            mutex.withLock {
                savedStateHandle["secretKeyState"] = _onboardingState.value.copy(
                    isCompleteLoading = false
                )
            }
        }.launchIn(scope = viewModelScope)
    }
}
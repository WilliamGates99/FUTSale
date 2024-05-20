package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Resource
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.states.OnboardingState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case.CompleteOnboardingUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.utils.OnboardingUiEvent
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val completeOnboardingUseCase: Lazy<CompleteOnboardingUseCase>
) : ViewModel() {

    val onboardingState = savedStateHandle.getStateFlow(
        key = "onboardingState",
        initialValue = OnboardingState()
    )

    private val _completeOnboardingEventChannel = Channel<Event>()
    val completeOnboardingEventChannel = _completeOnboardingEventChannel.receiveAsFlow()

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.PartnerIdChanged -> {
                savedStateHandle["onboardingState"] = onboardingState.value.copy(
                    partnerId = event.partnerId,
                    partnerIdErrorText = null
                )
            }
            is OnboardingEvent.SecretKeyChanged -> {
                savedStateHandle["secretKeyState"] = onboardingState.value.copy(
                    secretKey = event.secretKey,
                    secretKeyErrorText = null
                )
            }
            OnboardingEvent.SaveUserData -> saveUserData()
        }
    }

    private fun saveUserData() = viewModelScope.launch {
        savedStateHandle["secretKeyState"] = onboardingState.value.copy(
            isCompleteLoading = true
        )

        val result = completeOnboardingUseCase.get()(
            partnerId = onboardingState.value.partnerId.trim(),
            secretKey = onboardingState.value.secretKey.trim()
        )

        when (result) {
            is Resource.Success -> {
                _completeOnboardingEventChannel.send(OnboardingUiEvent.NavigateToHomeScreen)
                savedStateHandle["secretKeyState"] = onboardingState.value.copy(
                    isCompleteLoading = false
                )
            }
            is Resource.Error -> {
                result.message?.let { message ->
                    _completeOnboardingEventChannel.send(UiEvent.ShowSnackbar(message))
                }
                savedStateHandle["secretKeyState"] = onboardingState.value.copy(
                    isCompleteLoading = false
                )
            }
        }
    }
}
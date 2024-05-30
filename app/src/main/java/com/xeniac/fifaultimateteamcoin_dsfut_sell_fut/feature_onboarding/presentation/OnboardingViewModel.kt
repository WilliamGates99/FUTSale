package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.states.OnboardingState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case.CompleteOnboardingUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.utils.OnboardingError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation.utils.OnboardingUiEvent
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboardingUseCase: Lazy<CompleteOnboardingUseCase>,
    private val savedStateHandle: SavedStateHandle
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
                savedStateHandle["onboardingState"] = onboardingState.value.copy(
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
            is Result.Success -> {
                _completeOnboardingEventChannel.send(OnboardingUiEvent.NavigateToHomeScreen)
                savedStateHandle["secretKeyState"] = onboardingState.value.copy(
                    isCompleteLoading = false
                )
            }
            is Result.Error -> {
                when (result.error) {
                    OnboardingError.SomethingWentWrong -> {
                        _completeOnboardingEventChannel.send(
                            UiEvent.ShowShortSnackbar(UiText.StringResource(R.string.error_something_went_wrong))
                        )
                    }
                }

                savedStateHandle["secretKeyState"] = onboardingState.value.copy(
                    isCompleteLoading = false
                )
            }
        }
    }
}
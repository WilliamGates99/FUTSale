package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.states.CustomTextFieldState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Resource
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case.CompleteOnboardingUseCase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.util.OnboardingUiEvent
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

    val partnerIdState = savedStateHandle.getStateFlow(
        key = "partnerId",
        initialValue = CustomTextFieldState()
    )

    val secretKeyState = savedStateHandle.getStateFlow(
        key = "secretKey",
        initialValue = CustomTextFieldState()
    )

    private val _setIsOnboardingCompletedEventChannel = Channel<Event>()
    val setIsOnboardingCompletedEventChannel = _setIsOnboardingCompletedEventChannel.receiveAsFlow()

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.PartnerIdChanged -> {
                savedStateHandle["partnerId"] = savedStateHandle
                    .get<CustomTextFieldState>(key = "partnerId")?.copy(
                        text = event.partnerId,
                        errorText = null
                    )
            }
            is OnboardingEvent.SecretKeyChanged -> {
                savedStateHandle["secretKey"] = savedStateHandle
                    .get<CustomTextFieldState>(key = "secretKey")?.copy(
                        text = event.secretKey,
                        errorText = null
                    )
            }
            OnboardingEvent.SaveUserData -> saveUserData()
        }
    }

    private fun saveUserData() = viewModelScope.launch {
        val result = completeOnboardingUseCase.get()(
            partnerId = partnerIdState.value.text.trim(),
            secretKey = secretKeyState.value.text.trim()
        )

        when (result) {
            is Resource.Success -> {
                _setIsOnboardingCompletedEventChannel.send(OnboardingUiEvent.NavigateToHomeScreen)
            }
            is Resource.Error -> {
                result.message?.let { message ->
                    _setIsOnboardingCompletedEventChannel.send(UiEvent.ShowSnackbar(message))
                }
            }
        }
    }
}
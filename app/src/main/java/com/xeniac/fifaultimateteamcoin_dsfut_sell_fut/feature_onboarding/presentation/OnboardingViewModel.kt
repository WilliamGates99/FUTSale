package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.states.CustomTextFieldState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case.OnboardingUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.util.OnboardingUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingUseCases: OnboardingUseCases
) : ViewModel() {

    private val _partnerIdState = MutableStateFlow(CustomTextFieldState())
    val partnerIdState = _partnerIdState.asStateFlow()

    private val _secretKeyState = MutableStateFlow(CustomTextFieldState())
    val secretKeyState = _secretKeyState.asStateFlow()

    private val _setIsOnboardingCompletedEventChannel = Channel<Event>()
    val setIsOnboardingCompletedEventChannel = _setIsOnboardingCompletedEventChannel.receiveAsFlow()

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.PartnerIdChanged -> {
                _partnerIdState.value = partnerIdState.value.copy(
                    text = event.partnerId,
                    errorText = null
                )
            }
            is OnboardingEvent.SecretKeyChanged -> {
                _secretKeyState.value = secretKeyState.value.copy(
                    text = event.secretKey,
                    errorText = null
                )
            }
            OnboardingEvent.SaveUserData -> saveUserData()
        }
    }

    private fun saveUserData() = viewModelScope.launch {
        try {
            onboardingUseCases.apply {
                setPartnerIdUseCase.get()(partnerId = partnerIdState.value.text.trim())
                setSecretKeyUseCase.get()(secretKey = secretKeyState.value.text.trim())
                setIsOnboardingCompletedUseCase.get()(isCompleted = true)
            }

            _setIsOnboardingCompletedEventChannel.send(OnboardingUiEvent.NavigateToHomeScreen)
        } catch (e: Exception) {
            _setIsOnboardingCompletedEventChannel.send(
                UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_something_went_wrong))
            )
        }
    }
}
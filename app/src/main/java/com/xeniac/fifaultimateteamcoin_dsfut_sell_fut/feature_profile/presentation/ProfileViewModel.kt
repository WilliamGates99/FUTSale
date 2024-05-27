package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.states.ProfileState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases.ProfileUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.PartnerIdError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.SecretKeyError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val profileState = savedStateHandle.getStateFlow(
        key = "profileState",
        initialValue = ProfileState()
    )

    private val _updatePartnerIdEventChannel = Channel<Event>()
    val updatePartnerIdEventChannel = _updatePartnerIdEventChannel.receiveAsFlow()

    private val _updateSecretKeyEventChannel = Channel<Event>()
    val updateSecretKeyEventChannel = _updateSecretKeyEventChannel.receiveAsFlow()

    private var updatePartnerIdJob: Job? = null
    private var updateSecretKeyJob: Job? = null

    init {
        getProfile()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.GetProfile -> getProfile()
            is ProfileEvent.PartnerIdChanged -> {
                updatePartnerIdJob?.cancel()
                updatePartnerIdJob = updatePartnerId(event.partnerId.trim())
            }
            is ProfileEvent.SecretKeyChanged -> {
                updateSecretKeyJob?.cancel()
                updateSecretKeyJob = updatePartnerId(event.secretKey.trim())
            }
        }
    }

    private fun getProfile() = viewModelScope.launch {
        savedStateHandle["profileState"] = profileUseCases.getProfileUseCase.get()()
    }

    private fun updatePartnerId(partnerId: String): Job = viewModelScope.launch {
        savedStateHandle["profileState"] = profileState.value.copy(
            partnerId = partnerId,
            isPartnerIdLoading = true
        )

        delay(500.milliseconds)

        val updatePartnerIdResult = profileUseCases.updatePartnerIdUseCase.get()(
            partnerId = partnerId
        )

        when (updatePartnerIdResult.result) {
            is Result.Success -> {
                savedStateHandle["profileState"] = profileState.value.copy(
                    isPartnerIdSaved = true,
                    isPartnerIdLoading = false
                )
            }
            is Result.Error -> {
                when (updatePartnerIdResult.result.error) {
                    PartnerIdError.InvalidPartnerId -> {
                        savedStateHandle["profileState"] = profileState.value.copy(
                            partnerIdErrorText = UiText.StringResource(R.string.profile_textfield_partner_id_error_invalid),
                            isPartnerIdSaved = false,
                            isPartnerIdLoading = false
                        )
                    }
                    PartnerIdError.SomethingWentWrong -> {
                        savedStateHandle["profileState"] = profileState.value.copy(
                            isPartnerIdSaved = false,
                            isPartnerIdLoading = false
                        )

                        _updatePartnerIdEventChannel.send(
                            UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_something_went_wrong))
                        )
                    }
                }
            }
            null -> {
                savedStateHandle["profileState"] = profileState.value.copy(
                    isPartnerIdLoading = false
                )
            }
        }
    }

    private fun updateSecretKey(secretKey: String): Job = viewModelScope.launch {
        savedStateHandle["profileState"] = profileState.value.copy(
            secretKey = secretKey,
            isSecretKeyLoading = true
        )

        delay(500.milliseconds)

        val updateSecretKeyResult = profileUseCases.updateSecretKeyUseCase.get()(
            secretKey = secretKey
        )

        when (updateSecretKeyResult.result) {
            is Result.Success -> {
                savedStateHandle["profileState"] = profileState.value.copy(
                    isSecretKeySaved = true,
                    isSecretKeyLoading = false
                )
            }
            is Result.Error -> {
                when (updateSecretKeyResult.result.error) {
                    SecretKeyError.InvalidSecretKey -> {
                        savedStateHandle["profileState"] = profileState.value.copy(
                            partnerIdErrorText = UiText.StringResource(R.string.profile_textfield_secret_key_error_invalid),
                            isSecretKeySaved = false,
                            isSecretKeyLoading = false
                        )
                    }
                    SecretKeyError.SomethingWentWrong -> {
                        savedStateHandle["profileState"] = profileState.value.copy(
                            isSecretKeySaved = false,
                            isSecretKeyLoading = false
                        )

                        _updateSecretKeyEventChannel.send(
                            UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_something_went_wrong))
                        )
                    }
                }
            }
            null -> {
                savedStateHandle["profileState"] = profileState.value.copy(
                    isSecretKeyLoading = false
                )
            }
        }
    }
}
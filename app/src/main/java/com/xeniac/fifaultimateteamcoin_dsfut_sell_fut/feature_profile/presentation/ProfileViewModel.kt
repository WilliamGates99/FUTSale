package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases.ProfileUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.PartnerIdError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.SecretKeyError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.states.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mutex: Mutex = Mutex()

    private val _profileState = savedStateHandle.getStateFlow(
        key = "profileState",
        initialValue = ProfileState()
    )
    val profileState = _profileState.onStart {
        getProfile()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = ProfileState()
    )

    private val _updatePartnerIdEventChannel = Channel<UiEvent>()
    val updatePartnerIdEventChannel = _updatePartnerIdEventChannel.receiveAsFlow()

    private val _updateSecretKeyEventChannel = Channel<UiEvent>()
    val updateSecretKeyEventChannel = _updateSecretKeyEventChannel.receiveAsFlow()

    private var updatePartnerIdJob: Job? = null
    private var updateSecretKeyJob: Job? = null

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.GetProfile -> getProfile()
            is ProfileAction.PartnerIdChanged -> {
                updatePartnerIdJob?.cancel()
                updatePartnerIdJob = updatePartnerId(
                    partnerId = action.partnerId.filter { it.isDigit() }.trim()
                )
            }
            is ProfileAction.SecretKeyChanged -> {
                updateSecretKeyJob?.cancel()
                updateSecretKeyJob = updateSecretKey(secretKey = action.secretKey.trim())
            }
        }
    }

    private fun getProfile() = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["profileState"] = profileUseCases.getProfileUseCase.get()()
        }
    }

    private fun updatePartnerId(partnerId: String): Job = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["profileState"] = profileState.value.copy(
                partnerId = partnerId,
                partnerIdErrorText = null,
                isPartnerIdLoading = true
            )

            delay(500.milliseconds)

            val updatePartnerIdResult = profileUseCases.updatePartnerIdUseCase.get()(
                partnerId = partnerId
            )

            if (updatePartnerIdResult.partnerIdError != null) {
                checkPartnerIdError(updatePartnerIdResult.partnerIdError)
            }

            when (updatePartnerIdResult.result) {
                is Result.Success -> {
                    savedStateHandle["profileState"] = profileState.value.copy(
                        isPartnerIdSaved = true,
                        isPartnerIdLoading = false
                    )
                }
                is Result.Error -> {
                    checkPartnerIdError(updatePartnerIdResult.result.error)
                }
                null -> {
                    savedStateHandle["profileState"] = profileState.value.copy(
                        isPartnerIdLoading = false
                    )
                }
            }
        }
    }

    private suspend fun checkPartnerIdError(partnerIdError: PartnerIdError) {
        when (partnerIdError) {
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
                    UiEvent.ShowShortSnackbar(UiText.StringResource(R.string.error_something_went_wrong))
                )
            }
        }
    }

    private fun updateSecretKey(secretKey: String): Job = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["profileState"] = profileState.value.copy(
                secretKey = secretKey,
                secretKeyErrorText = null,
                isSecretKeyLoading = true
            )

            delay(500.milliseconds)

            val updateSecretKeyResult = profileUseCases.updateSecretKeyUseCase.get()(
                secretKey = secretKey
            )

            if (updateSecretKeyResult.secretKeyError != null) {
                checkSecretKeyError(updateSecretKeyResult.secretKeyError)
            }

            when (updateSecretKeyResult.result) {
                is Result.Success -> {
                    savedStateHandle["profileState"] = profileState.value.copy(
                        isSecretKeySaved = true,
                        isSecretKeyLoading = false
                    )
                }
                is Result.Error -> {
                    checkSecretKeyError(updateSecretKeyResult.result.error)
                }
                null -> {
                    savedStateHandle["profileState"] = profileState.value.copy(
                        isSecretKeyLoading = false
                    )
                }
            }
        }
    }

    private suspend fun checkSecretKeyError(secretKeyError: SecretKeyError) {
        when (secretKeyError) {
            SecretKeyError.InvalidSecretKey -> {
                savedStateHandle["profileState"] = profileState.value.copy(
                    secretKeyErrorText = UiText.StringResource(R.string.profile_textfield_secret_key_error_invalid),
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
                    UiEvent.ShowShortSnackbar(UiText.StringResource(R.string.error_something_went_wrong))
                )
            }
        }
    }
}
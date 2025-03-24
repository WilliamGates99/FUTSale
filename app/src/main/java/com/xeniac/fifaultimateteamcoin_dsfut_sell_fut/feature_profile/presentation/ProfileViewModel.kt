package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.convertDigitsToEnglish
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.toEnglishDigits
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases.ProfileUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.PartnerIdError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.SecretKeyError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.events.ProfileAction
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.states.ProfileState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.utils.asUiText
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
            is ProfileAction.PartnerIdChanged -> updatePartnerId(action.newValue)
            is ProfileAction.SecretKeyChanged -> updateSecretKey(action.newValue)
        }
    }

    private fun getProfile() = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["profileState"] = profileUseCases.getProfileUseCase.get()()
        }
    }

    private fun updatePartnerId(newValue: TextFieldValue) {
        updatePartnerIdJob?.cancel()
        updatePartnerIdJob = viewModelScope.launch {
            val newPartnerIdValue = newValue.copy(text = newValue.text.toEnglishDigits())

            mutex.withLock {
                savedStateHandle["profileState"] = _profileState.value.copy(
                    partnerIdState = _profileState.value.partnerIdState.copy(
                        value = newPartnerIdValue,
                        errorText = null
                    ),
                    isPartnerIdLoading = true
                )

                delay(500.milliseconds)

                val updatePartnerIdResult = profileUseCases.updatePartnerIdUseCase.get()(
                    partnerId = newPartnerIdValue.text
                )

                if (updatePartnerIdResult.partnerIdError != null) {
                    checkPartnerIdError(updatePartnerIdResult.partnerIdError)
                }

                when (updatePartnerIdResult.result) {
                    is Result.Success -> {
                        savedStateHandle["profileState"] = _profileState.value.copy(
                            isPartnerIdSaved = true,
                            isPartnerIdLoading = false
                        )
                    }
                    is Result.Error -> {
                        checkPartnerIdError(updatePartnerIdResult.result.error)
                    }
                    null -> {
                        savedStateHandle["profileState"] = _profileState.value.copy(
                            isPartnerIdLoading = false
                        )
                    }
                }
            }
        }
    }

    private suspend fun checkPartnerIdError(partnerIdError: PartnerIdError) {
        when (partnerIdError) {
            PartnerIdError.InvalidPartnerId -> {
                savedStateHandle["profileState"] = _profileState.value.copy(
                    partnerIdState = _profileState.value.partnerIdState.copy(
                        errorText = partnerIdError.asUiText()
                    ),
                    isPartnerIdSaved = false,
                    isPartnerIdLoading = false
                )
            }
            PartnerIdError.SomethingWentWrong -> {
                savedStateHandle["profileState"] = _profileState.value.copy(
                    isPartnerIdSaved = false,
                    isPartnerIdLoading = false
                )

                _updatePartnerIdEventChannel.send(
                    UiEvent.ShowShortSnackbar(partnerIdError.asUiText())
                )
            }
        }
    }

    private fun updateSecretKey(newValue: TextFieldValue) {
        updateSecretKeyJob?.cancel()
        updateSecretKeyJob = viewModelScope.launch {
            val newSecretKeyValue = newValue.copy(text = newValue.text.convertDigitsToEnglish())

            mutex.withLock {
                savedStateHandle["profileState"] = _profileState.value.copy(
                    secretKeyState = _profileState.value.secretKeyState.copy(
                        value = newSecretKeyValue,
                        errorText = null
                    ),
                    isSecretKeyLoading = true
                )

                delay(500.milliseconds)

                val updateSecretKeyResult = profileUseCases.updateSecretKeyUseCase.get()(
                    secretKey = newSecretKeyValue.text
                )

                if (updateSecretKeyResult.secretKeyError != null) {
                    checkSecretKeyError(updateSecretKeyResult.secretKeyError)
                }

                when (updateSecretKeyResult.result) {
                    is Result.Success -> {
                        savedStateHandle["profileState"] = _profileState.value.copy(
                            isSecretKeySaved = true,
                            isSecretKeyLoading = false
                        )
                    }
                    is Result.Error -> {
                        checkSecretKeyError(updateSecretKeyResult.result.error)
                    }
                    null -> {
                        savedStateHandle["profileState"] = _profileState.value.copy(
                            isSecretKeyLoading = false
                        )
                    }
                }
            }
        }
    }

    private suspend fun checkSecretKeyError(secretKeyError: SecretKeyError) {
        when (secretKeyError) {
            SecretKeyError.InvalidSecretKey -> {
                savedStateHandle["profileState"] = _profileState.value.copy(
                    secretKeyState = _profileState.value.secretKeyState.copy(
                        errorText = secretKeyError.asUiText()
                    ),
                    isSecretKeySaved = false,
                    isSecretKeyLoading = false
                )
            }
            SecretKeyError.SomethingWentWrong -> {
                savedStateHandle["profileState"] = _profileState.value.copy(
                    isSecretKeySaved = false,
                    isSecretKeyLoading = false
                )

                _updateSecretKeyEventChannel.send(
                    UiEvent.ShowShortSnackbar(secretKeyError.asUiText())
                )
            }
        }
    }
}
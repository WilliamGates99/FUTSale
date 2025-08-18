package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.convertDigitsToEnglish
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.toEnglishDigits
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.states.CustomTextFieldState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases.ProfileUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.events.ProfileAction
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.states.ProfileState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
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

    override fun onCleared() {
        updatePartnerIdJob?.cancel()
        updateSecretKeyJob?.cancel()
        super.onCleared()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.GetProfile -> getProfile()
            is ProfileAction.PartnerIdChanged -> updatePartnerId(action.newValue)
            is ProfileAction.SecretKeyChanged -> updateSecretKey(action.newValue)
        }
    }

    private fun getProfile() {
        profileUseCases.getPartnerIdUseCase.get()().zip(
            other = profileUseCases.getSecretKeyUseCase.get()(),
            transform = { partnerId, secretKey ->
                mutex.withLock {
                    savedStateHandle["profileState"] = _profileState.value.copy(
                        partnerIdState = CustomTextFieldState(
                            value = TextFieldValue(text = partnerId.orEmpty())
                        ),
                        secretKeyState = CustomTextFieldState(
                            value = TextFieldValue(text = secretKey.orEmpty())
                        ),
                        isPartnerIdSaved = !partnerId.isNullOrBlank(),
                        isSecretKeySaved = !secretKey.isNullOrBlank()
                    )
                }
            }
        ).launchIn(scope = viewModelScope)
    }

    private fun updatePartnerId(newValue: TextFieldValue) {
        updatePartnerIdJob?.cancel()

        val newPartnerIdValue = newValue.copy(text = newValue.text.toEnglishDigits())

        updatePartnerIdJob = profileUseCases.updatePartnerIdUseCase.get()(
            partnerId = newPartnerIdValue.text
        ).onStart {
            mutex.withLock {
                savedStateHandle["profileState"] = _profileState.value.copy(
                    partnerIdState = _profileState.value.partnerIdState.copy(
                        value = newPartnerIdValue,
                        errorText = null
                    ),
                    isPartnerIdLoading = true
                )
            }
        }.onEach { updatePartnerIdResult ->
            val hasUpdatePartnerIdError = updatePartnerIdResult.updatePartnerIdError != null
            if (hasUpdatePartnerIdError) {
                mutex.withLock {
                    savedStateHandle["profileState"] = _profileState.value.copy(
                        partnerIdState = _profileState.value.partnerIdState.copy(
                            errorText = updatePartnerIdResult.updatePartnerIdError!!.asUiText()
                        ),
                        isPartnerIdSaved = false
                    )
                }
            }

            when (val result = updatePartnerIdResult.result) {
                is Result.Success -> mutex.withLock {
                    savedStateHandle["profileState"] = _profileState.value.copy(
                        isPartnerIdSaved = true
                    )
                }
                is Result.Error -> {
                    mutex.withLock {
                        savedStateHandle["profileState"] = _profileState.value.copy(
                            isPartnerIdSaved = false
                        )
                    }

                    _updatePartnerIdEventChannel.send(
                        UiEvent.ShowShortSnackbar(result.error.asUiText())
                    )
                }
                null -> Unit
            }
        }.onCompletion {
            mutex.withLock {
                savedStateHandle["profileState"] = _profileState.value.copy(
                    isPartnerIdLoading = false
                )
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun updateSecretKey(newValue: TextFieldValue) {
        updateSecretKeyJob?.cancel()

        val newSecretKeyValue = newValue.copy(text = newValue.text.convertDigitsToEnglish())

        updateSecretKeyJob = profileUseCases.updateSecretKeyUseCase.get()(
            secretKey = newSecretKeyValue.text
        ).onStart {
            mutex.withLock {
                savedStateHandle["profileState"] = _profileState.value.copy(
                    secretKeyState = _profileState.value.secretKeyState.copy(
                        value = newSecretKeyValue,
                        errorText = null
                    ),
                    isSecretKeyLoading = true
                )
            }
        }.onEach { updateSecretKeyResult ->
            val hasUpdateSecretKeyError = updateSecretKeyResult.updateSecretKeyError != null
            if (hasUpdateSecretKeyError) {
                mutex.withLock {
                    savedStateHandle["profileState"] = _profileState.value.copy(
                        secretKeyState = _profileState.value.secretKeyState.copy(
                            errorText = updateSecretKeyResult.updateSecretKeyError!!.asUiText()
                        ),
                        isSecretKeySaved = false
                    )
                }
            }

            when (val result = updateSecretKeyResult.result) {
                is Result.Success -> {
                    savedStateHandle["profileState"] = _profileState.value.copy(
                        isSecretKeySaved = true
                    )
                }
                is Result.Error -> {
                    mutex.withLock {
                        savedStateHandle["profileState"] = _profileState.value.copy(
                            isSecretKeySaved = false
                        )
                    }

                    _updateSecretKeyEventChannel.send(
                        UiEvent.ShowShortSnackbar(result.error.asUiText())
                    )
                }
                null -> Unit
            }
        }.onCompletion {
            mutex.withLock {
                savedStateHandle["profileState"] = _profileState.value.copy(
                    isSecretKeyLoading = false
                )
            }
        }.launchIn(scope = viewModelScope)
    }
}
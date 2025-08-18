package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.toEnglishDigits
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.NetworkObserverHelper.hasNetworkConnection
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.PickUpPlayerUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.events.PickUpPlayerAction
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.events.PickUpPlayerUiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.states.PickUpPlayerState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PickUpPlayerViewModel @Inject constructor(
    private val pickUpPlayerUseCases: PickUpPlayerUseCases,
    private val decimalFormat: DecimalFormat,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mutex: Mutex = Mutex()

    private val _pickUpPlayerState = savedStateHandle.getStateFlow(
        key = "pickUpPlayerState",
        initialValue = PickUpPlayerState()
    )
    val pickUpPlayerState = combine(
        flow = _pickUpPlayerState,
        flow2 = pickUpPlayerUseCases.getIsNotificationSoundEnabledUseCase.get()(),
        flow3 = pickUpPlayerUseCases.getIsNotificationVibrateEnabledUseCase.get()(),
        flow4 = pickUpPlayerUseCases.observeLatestPickedUpPlayersUseCase.get()(),
        flow5 = pickUpPlayerUseCases.getSelectedPlatformUseCase.get()()
    ) { pickUpPlayerState, isNotificationSoundEnabled, isNotificationVibrateEnabled, latestPickedUpPlayers, selectedPlatform ->
        mutex.withLock {
            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.copy(
                isNotificationSoundEnabled = isNotificationSoundEnabled,
                isNotificationVibrateEnabled = isNotificationVibrateEnabled,
                latestPickedUpPlayers = latestPickedUpPlayers,
                selectedPlatform = selectedPlatform
            )
        }
        _pickUpPlayerState.value
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = PickUpPlayerState()
    )

    private val _timerText = MutableStateFlow<UiText>(
        UiText.StringResource(
            R.string.pick_up_player_latest_player_timer,
            decimalFormat.format(0),
            decimalFormat.format(0)
        )
    )
    val timerText = _timerText.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = UiText.StringResource(
            R.string.pick_up_player_latest_player_timer,
            decimalFormat.format(0),
            decimalFormat.format(0)
        )
    )

    private val _changePlatformEventChannel = Channel<Event>()
    val changePlatformEventChannel = _changePlatformEventChannel.receiveAsFlow()

    private val _autoPickUpPlayerEventChannel = Channel<Event>()
    val autoPickUpPlayerEventChannel = _autoPickUpPlayerEventChannel.receiveAsFlow()

    private val _pickUpPlayerOnceEventChannel = Channel<Event>()
    val pickUpPlayerOnceEventChannel = _pickUpPlayerOnceEventChannel.receiveAsFlow()

    private var autoPickUpPlayerJob: Job? = null
    private var countDownTimerJob: Job? = null

    override fun onCleared() {
        autoPickUpPlayerJob?.cancel()
        countDownTimerJob?.cancel()
        super.onCleared()
    }

    fun onAction(action: PickUpPlayerAction) {
        when (action) {
            is PickUpPlayerAction.PlatformChanged -> setSelectedPlatform(action.platform)
            is PickUpPlayerAction.MinPriceChanged -> minPriceChanged(action.newValue)
            is PickUpPlayerAction.MaxPriceChanged -> maxPriceChanged(action.newValue)
            is PickUpPlayerAction.TakeAfterCheckedChanged -> takeAfterCheckedChanged(action.isChecked)
            is PickUpPlayerAction.TakeAfterSliderChanged -> takeAfterSliderChanged(action.delayInSeconds)
            PickUpPlayerAction.CancelAutoPickUpPlayer -> cancelAutoPickUpPlayer()
            PickUpPlayerAction.AutoPickUpPlayer -> autoPickUpPlayer()
            PickUpPlayerAction.PickUpPlayerOnce -> pickUpPlayerOnce()
            is PickUpPlayerAction.StartCountDownTimer -> startCountDownTimer(action.expiryTimeInMs)
        }
    }

    private fun setSelectedPlatform(platform: Platform) {
        pickUpPlayerUseCases.storeSelectedPlatformUseCase.get()(platform).onEach { result ->
            when (result) {
                is Result.Success -> Unit
                is Result.Error -> {
                    _changePlatformEventChannel.send(UiEvent.ShowShortSnackbar(result.error.asUiText()))
                }
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun minPriceChanged(newValue: TextFieldValue) = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                minPriceState = _pickUpPlayerState.value.minPriceState.copy(
                    value = newValue.copy(text = newValue.text.toEnglishDigits()),
                    errorText = null
                )
            )
        }
    }

    private fun maxPriceChanged(newValue: TextFieldValue) = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                maxPriceState = _pickUpPlayerState.value.maxPriceState.copy(
                    value = newValue.copy(text = newValue.text.toEnglishDigits()),
                    errorText = null
                )
            )
        }
    }

    private fun takeAfterCheckedChanged(isChecked: Boolean) = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                isTakeAfterChecked = isChecked,
                takeAfterDelayInSeconds = if (isChecked) _pickUpPlayerState.value.takeAfterDelayInSeconds else 0,
                takeAfterErrorText = null
            )
        }
    }

    private fun takeAfterSliderChanged(delayInSeconds: Int) = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                takeAfterDelayInSeconds = delayInSeconds,
                takeAfterErrorText = null
            )
        }
    }

    private fun cancelAutoPickUpPlayer() = viewModelScope.launch {
        autoPickUpPlayerJob?.cancel()
        mutex.withLock {
            savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                isAutoPickUpLoading = false
            )
        }
    }

    private fun autoPickUpPlayer() {
        autoPickUpPlayerJob?.cancel()

        if (!hasNetworkConnection()) {
            savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                isAutoPickUpLoading = false
            )
            _autoPickUpPlayerEventChannel.trySend(UiEvent.ShowOfflineSnackbar)
            return
        }

        autoPickUpPlayerJob = pickUpPlayerUseCases.pickUpPlayerUseCase.get()(
            minPrice = _pickUpPlayerState.value.minPriceState.value.text.ifBlank { null },
            maxPrice = _pickUpPlayerState.value.maxPriceState.value.text.ifBlank { null },
            takeAfterDelayInSeconds = with(_pickUpPlayerState.value) {
                if (isTakeAfterChecked) takeAfterDelayInSeconds else null
            }
        ).onStart {
            mutex.withLock {
                savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                    isAutoPickUpLoading = true
                )
            }
        }.onEach { pickUpPlayerResult ->
            val hasPartnerIdError = pickUpPlayerResult.partnerIdError != null
            val hasSecretKeyError = pickUpPlayerResult.secretKeyError != null
            val hasPartnerIdAndSecretKeyError = hasPartnerIdError && hasSecretKeyError
            when {
                hasPartnerIdAndSecretKeyError -> {
                    _autoPickUpPlayerEventChannel.send(
                        PickUpPlayerUiEvent.ShowPartnerIdAndSecretKeySnackbar
                    )
                }
                hasPartnerIdError -> {
                    _autoPickUpPlayerEventChannel.send(
                        PickUpPlayerUiEvent.ShowPartnerIdSnackbar(
                            pickUpPlayerResult.partnerIdError!!.asUiText()
                        )
                    )
                }
                hasSecretKeyError -> {
                    _autoPickUpPlayerEventChannel.send(
                        PickUpPlayerUiEvent.ShowSecretKeySnackbar(
                            pickUpPlayerResult.secretKeyError!!.asUiText()
                        )
                    )
                }
            }

            val hasMinPriceError = pickUpPlayerResult.minPriceError != null
            if (hasMinPriceError) {
                mutex.withLock {
                    savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                        minPriceState = _pickUpPlayerState.value.minPriceState.copy(
                            errorText = pickUpPlayerResult.minPriceError!!.asUiText()
                        )
                    )
                }
            }

            val hasMaxPriceError = pickUpPlayerResult.maxPriceError != null
            if (hasMaxPriceError) {
                mutex.withLock {
                    savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                        maxPriceState = _pickUpPlayerState.value.maxPriceState.copy(
                            errorText = pickUpPlayerResult.maxPriceError!!.asUiText()
                        )
                    )
                }
            }

            val hasTakeAfterError = pickUpPlayerResult.takeAfterError != null
            if (hasTakeAfterError) {
                mutex.withLock {
                    savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                        takeAfterErrorText = pickUpPlayerResult.takeAfterError!!.asUiText()
                    )
                }
            }

            when (val result = pickUpPlayerResult.result) {
                is Result.Success -> result.data.let { player ->
                    _autoPickUpPlayerEventChannel.send(
                        PickUpPlayerUiEvent.ShowSuccessNotification(player.name)
                    )
                    _autoPickUpPlayerEventChannel.send(
                        PickUpPlayerUiEvent.NavigateToPickedUpPlayerInfoScreen(player.id!!)
                    )
                }
                is Result.Error -> {
                    when (val error = result.error) {
                        PickUpPlayerError.Network.DsfutEmpty -> {
                            delay(Constants.AUTO_PICK_UP_DELAY_IN_MILLIS)
                            autoPickUpPlayer()
                        }
                        PickUpPlayerError.Network.DsfutSignature -> {
                            _autoPickUpPlayerEventChannel.send(
                                PickUpPlayerUiEvent.ShowSignatureSnackbar(error.asUiText())
                            )
                        }
                        else -> {
                            _autoPickUpPlayerEventChannel.send(
                                PickUpPlayerUiEvent.ShowErrorNotification(error.asUiText())
                            )
                            _autoPickUpPlayerEventChannel.send(
                                UiEvent.ShowLongSnackbar(error.asUiText())
                            )
                        }
                    }
                }
                null -> Unit
            }
        }.onCompletion {
            mutex.withLock {
                savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                    isAutoPickUpLoading = false
                )
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun pickUpPlayerOnce() {
        if (!hasNetworkConnection()) {
            _pickUpPlayerOnceEventChannel.trySend(UiEvent.ShowOfflineSnackbar)
            return
        }

        pickUpPlayerUseCases.pickUpPlayerUseCase.get()(
            minPrice = _pickUpPlayerState.value.minPriceState.value.text.ifBlank { null },
            maxPrice = _pickUpPlayerState.value.maxPriceState.value.text.ifBlank { null },
            takeAfterDelayInSeconds = with(_pickUpPlayerState.value) {
                if (isTakeAfterChecked) takeAfterDelayInSeconds else null
            }
        ).onStart {
            mutex.withLock {
                savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                    isPickUpOnceLoading = true
                )
            }
        }.onEach { pickUpPlayerResult ->
            val hasPartnerIdError = pickUpPlayerResult.partnerIdError != null
            val hasSecretKeyError = pickUpPlayerResult.secretKeyError != null
            val hasPartnerIdAndSecretKeyError = hasPartnerIdError && hasSecretKeyError

            when {
                hasPartnerIdAndSecretKeyError -> {
                    _pickUpPlayerOnceEventChannel.send(
                        PickUpPlayerUiEvent.ShowPartnerIdAndSecretKeySnackbar
                    )
                }
                hasPartnerIdError -> {
                    _pickUpPlayerOnceEventChannel.send(
                        PickUpPlayerUiEvent.ShowPartnerIdSnackbar(
                            pickUpPlayerResult.partnerIdError!!.asUiText()
                        )
                    )
                }
                hasSecretKeyError -> {
                    _pickUpPlayerOnceEventChannel.send(
                        PickUpPlayerUiEvent.ShowSecretKeySnackbar(
                            pickUpPlayerResult.secretKeyError!!.asUiText()
                        )
                    )
                }
            }

            val hasMinPriceError = pickUpPlayerResult.minPriceError != null
            if (hasMinPriceError) {
                mutex.withLock {
                    savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                        minPriceState = _pickUpPlayerState.value.minPriceState.copy(
                            errorText = pickUpPlayerResult.minPriceError!!.asUiText()
                        )
                    )
                }
            }

            val hasMaxPriceError = pickUpPlayerResult.maxPriceError != null
            if (hasMaxPriceError) {
                mutex.withLock {
                    savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                        maxPriceState = _pickUpPlayerState.value.maxPriceState.copy(
                            errorText = pickUpPlayerResult.maxPriceError!!.asUiText()
                        )
                    )
                }
            }

            val hasTakeAfterError = pickUpPlayerResult.takeAfterError != null
            if (hasTakeAfterError) {
                mutex.withLock {
                    savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                        takeAfterErrorText = pickUpPlayerResult.takeAfterError!!.asUiText()
                    )
                }
            }

            when (val result = pickUpPlayerResult.result) {
                is Result.Success -> result.data.let { player ->
                    _pickUpPlayerOnceEventChannel.send(
                        PickUpPlayerUiEvent.NavigateToPickedUpPlayerInfoScreen(player.id!!)
                    )
                }
                is Result.Error -> {
                    when (val error = result.error) {
                        PickUpPlayerError.Network.DsfutSignature -> {
                            _pickUpPlayerOnceEventChannel.send(
                                PickUpPlayerUiEvent.ShowSignatureSnackbar(error.asUiText())
                            )
                        }
                        else -> {
                            _pickUpPlayerOnceEventChannel.send(
                                UiEvent.ShowLongSnackbar(error.asUiText())
                            )
                        }
                    }
                }
                null -> Unit
            }
        }.onCompletion {
            mutex.withLock {
                savedStateHandle["pickUpPlayerState"] = _pickUpPlayerState.value.copy(
                    isPickUpOnceLoading = false
                )
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun startCountDownTimer(expiryTimeInMs: Long) {
        countDownTimerJob?.cancel()
        countDownTimerJob = pickUpPlayerUseCases.startCountDownTimerUseCase.get()(
            expiryTimeInMs = expiryTimeInMs
        ).onEach { timerValueInSeconds ->
            _timerText.update {
                val isTimerFinished = timerValueInSeconds == 0
                if (isTimerFinished) {
                    return@update UiText.StringResource(R.string.pick_up_player_latest_player_timer_expired)
                }

                val minutes = decimalFormat.format(timerValueInSeconds / 60)
                val seconds = decimalFormat.format(timerValueInSeconds % 60)

                UiText.StringResource(
                    R.string.pick_up_player_latest_player_timer,
                    minutes,
                    seconds
                )
            }
        }.launchIn(scope = viewModelScope)
    }
}
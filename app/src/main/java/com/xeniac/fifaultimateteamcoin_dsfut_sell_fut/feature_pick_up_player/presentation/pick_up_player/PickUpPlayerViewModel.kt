package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.ConnectivityObserver
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.NetworkObserverHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.PickUpPlayerUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.states.PickUpPlayerState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils.PickUpPlayerUiEvent
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
import kotlinx.coroutines.flow.onEach
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
        flow4 = pickUpPlayerUseCases.observeLatestPickedPlayersUseCase.get()(),
        flow5 = pickUpPlayerUseCases.getSelectedPlatformUseCase.get()()
    ) { pickUpPlayerState, isNotificationSoundEnabled, isNotificationVibrateEnabled, latestPickedPlayers, selectedPlatform ->
        pickUpPlayerState.copy(
            isNotificationSoundEnabled = isNotificationSoundEnabled,
            isNotificationVibrateEnabled = isNotificationVibrateEnabled,
            latestPickedPlayers = latestPickedPlayers,
            selectedPlatform = selectedPlatform
        )
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

    fun onAction(action: PickUpPlayerAction) {
        when (action) {
            is PickUpPlayerAction.PlatformChanged -> setSelectedPlatform(platform = action.platform)
            is PickUpPlayerAction.MinPriceChanged -> minPriceChanged(action.minPrice)
            is PickUpPlayerAction.MaxPriceChanged -> maxPriceChanged(action.maxPrice)
            is PickUpPlayerAction.TakeAfterCheckedChanged -> takeAfterCheckedChanged(action.isChecked)
            is PickUpPlayerAction.TakeAfterSliderChanged -> takeAfterSliderChanged(action.delayInSeconds)
            PickUpPlayerAction.CancelAutoPickUpPlayer -> cancelAutoPickUpPlayer()
            PickUpPlayerAction.AutoPickUpPlayer -> autoPickUpPlayer()
            PickUpPlayerAction.PickUpPlayerOnce -> pickUpPlayerOnce()
            is PickUpPlayerAction.StartCountDownTimer -> startCountDownTimer(action.expiryTimeInMs)
        }
    }

    private fun setSelectedPlatform(platform: Platform) = viewModelScope.launch {
        when (val result = pickUpPlayerUseCases.storeSelectedPlatformUseCase.get()(platform)) {
            is Result.Success -> Unit
            is Result.Error -> {
                _changePlatformEventChannel.send(UiEvent.ShowShortSnackbar(result.error.asUiText()))
            }
        }
    }

    private fun minPriceChanged(minPrice: String) = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                minPrice = minPrice.filter { it.isDigit() }.trim(),
                minPriceErrorText = null
            )
        }
    }

    private fun maxPriceChanged(maxPrice: String) = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                maxPrice = maxPrice.filter { it.isDigit() }.trim(),
                maxPriceErrorText = null
            )
        }
    }

    private fun takeAfterCheckedChanged(isChecked: Boolean) = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                isTakeAfterChecked = isChecked,
                takeAfterDelayInSeconds = if (isChecked) pickUpPlayerState.value.takeAfterDelayInSeconds else 0,
                takeAfterErrorText = null
            )
        }
    }

    private fun takeAfterSliderChanged(delayInSeconds: Int) = viewModelScope.launch {
        mutex.withLock {
            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                takeAfterDelayInSeconds = delayInSeconds,
                takeAfterErrorText = null
            )
        }
    }

    private fun cancelAutoPickUpPlayer() = viewModelScope.launch {
        autoPickUpPlayerJob?.cancel()
        mutex.withLock {
            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                isAutoPickUpLoading = false
            )
        }
    }

    private fun autoPickUpPlayer() {
        autoPickUpPlayerJob?.cancel()
        autoPickUpPlayerJob = viewModelScope.launch {
            if (NetworkObserverHelper.networkStatus.value == ConnectivityObserver.Status.AVAILABLE) {
                mutex.withLock {
                    savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                        isAutoPickUpLoading = true
                    )
                }

                val pickUpPlayerResult = pickUpPlayerUseCases.pickUpPlayerUseCase.get()(
                    minPrice = pickUpPlayerState.value.minPrice.ifBlank { null },
                    maxPrice = pickUpPlayerState.value.maxPrice.ifBlank { null },
                    takeAfterDelayInSeconds = if (pickUpPlayerState.value.isTakeAfterChecked) pickUpPlayerState.value.takeAfterDelayInSeconds else null
                )

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

                if (pickUpPlayerResult.minPriceError != null) {
                    when (val error = pickUpPlayerResult.minPriceError) {
                        PickUpPlayerError.InvalidMinPrice -> mutex.withLock {
                            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                                minPriceErrorText = error.asUiText()
                            )
                        }
                        else -> Unit
                    }
                }

                if (pickUpPlayerResult.maxPriceError != null) {
                    when (val error = pickUpPlayerResult.maxPriceError) {
                        PickUpPlayerError.InvalidMaxPrice -> mutex.withLock {
                            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                                maxPriceErrorText = error.asUiText()
                            )
                        }
                        else -> Unit
                    }
                }

                if (pickUpPlayerResult.takeAfterError != null) {
                    when (val error = pickUpPlayerResult.takeAfterError) {
                        PickUpPlayerError.InvalidTakeAfter -> mutex.withLock {
                            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                                takeAfterErrorText = error.asUiText()
                            )
                        }
                        else -> Unit
                    }
                }

                when (val result = pickUpPlayerResult.result) {
                    is Result.Success -> result.data.let { player ->
                        mutex.withLock {
                            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                                isAutoPickUpLoading = false
                            )
                        }

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

                                mutex.withLock {
                                    savedStateHandle["pickUpPlayerState"] =
                                        pickUpPlayerState.value.copy(
                                            isAutoPickUpLoading = false
                                        )
                                }
                            }
                            else -> {
                                _autoPickUpPlayerEventChannel.send(
                                    PickUpPlayerUiEvent.ShowErrorNotification(error.asUiText())
                                )
                                _autoPickUpPlayerEventChannel.send(
                                    UiEvent.ShowLongSnackbar(error.asUiText())
                                )

                                mutex.withLock {
                                    savedStateHandle["pickUpPlayerState"] =
                                        pickUpPlayerState.value.copy(
                                            isAutoPickUpLoading = false
                                        )
                                }
                            }
                        }
                    }
                    null -> mutex.withLock {
                        savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                            isAutoPickUpLoading = false
                        )
                    }
                }
            } else {
                _autoPickUpPlayerEventChannel.send(UiEvent.ShowOfflineSnackbar)
            }
        }
    }

    private fun pickUpPlayerOnce() = viewModelScope.launch {
        if (NetworkObserverHelper.networkStatus.value == ConnectivityObserver.Status.AVAILABLE) {
            mutex.withLock {
                savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                    isPickUpOnceLoading = true
                )
            }

            val pickUpPlayerResult = pickUpPlayerUseCases.pickUpPlayerUseCase.get()(
                minPrice = pickUpPlayerState.value.minPrice.ifBlank { null },
                maxPrice = pickUpPlayerState.value.maxPrice.ifBlank { null },
                takeAfterDelayInSeconds = if (pickUpPlayerState.value.isTakeAfterChecked) pickUpPlayerState.value.takeAfterDelayInSeconds else null
            )

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

            if (pickUpPlayerResult.minPriceError != null) {
                when (val error = pickUpPlayerResult.minPriceError) {
                    PickUpPlayerError.InvalidMinPrice -> mutex.withLock {
                        savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                            minPriceErrorText = error.asUiText()
                        )
                    }
                    else -> Unit
                }
            }

            if (pickUpPlayerResult.maxPriceError != null) {
                when (val error = pickUpPlayerResult.maxPriceError) {
                    PickUpPlayerError.InvalidMaxPrice -> mutex.withLock {
                        savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                            maxPriceErrorText = error.asUiText()
                        )
                    }
                    else -> Unit
                }
            }

            if (pickUpPlayerResult.takeAfterError != null) {
                when (val error = pickUpPlayerResult.takeAfterError) {
                    PickUpPlayerError.InvalidTakeAfter -> mutex.withLock {
                        savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                            takeAfterErrorText = error.asUiText()
                        )
                    }
                    else -> Unit
                }
            }

            when (val result = pickUpPlayerResult.result) {
                is Result.Success -> result.data.let { player ->
                    mutex.withLock {
                        savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                            isPickUpOnceLoading = false
                        )
                    }

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

                            mutex.withLock {
                                savedStateHandle["pickUpPlayerState"] =
                                    pickUpPlayerState.value.copy(
                                        isPickUpOnceLoading = false
                                    )
                            }
                        }
                        else -> {
                            _pickUpPlayerOnceEventChannel.send(
                                UiEvent.ShowLongSnackbar(error.asUiText())
                            )

                            mutex.withLock {
                                savedStateHandle["pickUpPlayerState"] =
                                    pickUpPlayerState.value.copy(
                                        isPickUpOnceLoading = false
                                    )
                            }
                        }
                    }
                }
                null -> mutex.withLock {
                    savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                        isPickUpOnceLoading = false
                    )
                }
            }
        } else {
            _pickUpPlayerOnceEventChannel.send(UiEvent.ShowOfflineSnackbar)
        }
    }

    private fun startCountDownTimer(expiryTimeInMs: Long) {
        countDownTimerJob?.cancel()
        countDownTimerJob = pickUpPlayerUseCases.startCountDownTimerUseCase.get()(
            expiryTimeInMs = expiryTimeInMs
        ).onEach { timerValueInSeconds ->
            _timerText.update {
                val isTimerFinished = timerValueInSeconds == 0
                if (isTimerFinished) {
                    UiText.StringResource(R.string.pick_up_player_latest_player_timer_expired)
                } else {
                    val minutes = decimalFormat.format(timerValueInSeconds / 60)
                    val seconds = decimalFormat.format(timerValueInSeconds % 60)

                    UiText.StringResource(
                        R.string.pick_up_player_latest_player_timer,
                        minutes,
                        seconds
                    )
                }
            }
        }.launchIn(scope = viewModelScope)
    }
}
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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.states.PickUpPlayerState
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.PickUpPlayerUseCases
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PlatformError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils.PickUpPlayerUiEvent
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.pick_up_player.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickUpPlayerViewModel @Inject constructor(
    private val pickUpPlayerUseCases: PickUpPlayerUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun observeThreeLatestPlayers() = pickUpPlayerUseCases.observeThreeLatestPlayersUseCase.get()()

    val pickUpPlayerState = savedStateHandle.getStateFlow(
        key = "pickUpPlayerState",
        initialValue = PickUpPlayerState()
    )

    private val _changePlatformEventChannel = Channel<Event>()
    val changePlatformEventChannel = _changePlatformEventChannel.receiveAsFlow()

    private val _autoPickUpPlayerEventChannel = Channel<Event>()
    val autoPickUpPlayerEventChannel = _autoPickUpPlayerEventChannel.receiveAsFlow()

    private val _pickUpPlayerOnceEventChannel = Channel<Event>()
    val pickUpPlayerOnceEventChannel = _pickUpPlayerOnceEventChannel.receiveAsFlow()

    private var autoPickUpPlayerJob: Job? = null

    init {
        getSelectedPlatform()
    }

    fun onEvent(event: PickUpPlayerEvent) {
        when (event) {
            PickUpPlayerEvent.GetSelectedPlatform -> getSelectedPlatform()
            is PickUpPlayerEvent.PlatformChanged -> setSelectedPlatform(platform = event.platform)
            is PickUpPlayerEvent.MinPriceChanged -> {
                savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                    minPrice = event.minPrice,
                    minPriceErrorText = null
                )
            }
            is PickUpPlayerEvent.MaxPriceChanged -> {
                savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                    maxPrice = event.maxPrice,
                    maxPriceErrorText = null
                )
            }
            is PickUpPlayerEvent.TakeAfterCheckedChanged -> {
                savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                    isTakeAfterChecked = event.isChecked,
                    takeAfterDelayInSeconds = if (event.isChecked) pickUpPlayerState.value.takeAfterDelayInSeconds else 0,
                    takeAfterErrorText = null
                )
            }
            is PickUpPlayerEvent.TakeAfterSliderChanged -> {
                savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                    takeAfterDelayInSeconds = event.delayInSeconds,
                    takeAfterErrorText = null
                )
            }
            PickUpPlayerEvent.CancelAutoPickUpPlayer -> cancelAutoPickUpPlayer()
            PickUpPlayerEvent.AutoPickUpPlayer -> autoPickUpPlayer()
            PickUpPlayerEvent.PickUpPlayerOnce -> pickUpPlayerOnce()
        }
    }

    private fun getSelectedPlatform() = viewModelScope.launch {
        savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
            selectedPlatform = pickUpPlayerUseCases.getSelectedPlatformUseCase.get()()
        )
    }

    private fun setSelectedPlatform(platform: Platform) = viewModelScope.launch {
        when (val result = pickUpPlayerUseCases.setSelectedPlatformUseCase.get()(platform)) {
            is Result.Success -> {
                savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                    selectedPlatform = platform
                )
            }
            is Result.Error -> {
                when (result.error) {
                    PlatformError.SomethingWentWrong -> {
                        _changePlatformEventChannel.send(
                            UiEvent.ShowShortSnackbar(UiText.StringResource(R.string.error_something_went_wrong))
                        )
                    }
                }
            }
        }
    }

    private fun cancelAutoPickUpPlayer() {
        autoPickUpPlayerJob?.cancel()
    }

    private fun autoPickUpPlayer() {
        autoPickUpPlayerJob = viewModelScope.launch {
            if (NetworkObserverHelper.networkStatus == ConnectivityObserver.Status.AVAILABLE) {
                savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                    isAutoPickUpLoading = true
                )

                val pickUpPlayerResult = pickUpPlayerUseCases.pickUpPlayerUseCase.get()(
                    minPrice = pickUpPlayerState.value.minPrice,
                    maxPrice = pickUpPlayerState.value.maxPrice,
                    takeAfterDelayInSeconds = pickUpPlayerState.value.takeAfterDelayInSeconds
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
                        PickUpPlayerError.InvalidMinPrice -> {
                            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                                minPriceErrorText = error.asUiText()
                            )
                        }
                        else -> Unit
                    }
                }

                if (pickUpPlayerResult.maxPriceError != null) {
                    when (val error = pickUpPlayerResult.maxPriceError) {
                        PickUpPlayerError.InvalidMaxPrice -> {
                            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                                maxPriceErrorText = error.asUiText()
                            )
                        }
                        else -> Unit
                    }
                }

                if (pickUpPlayerResult.takeAfterError != null) {
                    when (val error = pickUpPlayerResult.takeAfterError) {
                        PickUpPlayerError.InvalidTakeAfter -> {
                            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                                takeAfterErrorText = error.asUiText()
                            )
                        }
                        else -> Unit
                    }
                }

                when (val result = pickUpPlayerResult.result) {
                    is Result.Success -> {
                        result.data.let { player ->
                            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                                isAutoPickUpLoading = false
                            )

                            _autoPickUpPlayerEventChannel.send(
                                PickUpPlayerUiEvent.ShowPlayerPickedUpSuccessfullyNotification
                            )
                            _autoPickUpPlayerEventChannel.send(
                                PickUpPlayerUiEvent.NavigateToPickedUpPlayerInfoScreen(player)
                            )
                        }
                    }
                    is Result.Error -> {
                        when (val error = result.error) {
                            PickUpPlayerError.Network.DsfutEmpty -> {
                                delay(Constants.AUTO_PICK_UP_DELAY_IN_MILLIS)
                                autoPickUpPlayer()
                            }
                            PickUpPlayerError.CancellationException -> {
                                savedStateHandle["pickUpPlayerState"] =
                                    pickUpPlayerState.value.copy(
                                        isAutoPickUpLoading = false
                                    )
                            }
                            PickUpPlayerError.Network.DsfutSignature -> {
                                _autoPickUpPlayerEventChannel.send(
                                    PickUpPlayerUiEvent.ShowSignatureSnackbar(error.asUiText())
                                )

                                savedStateHandle["pickUpPlayerState"] =
                                    pickUpPlayerState.value.copy(
                                        isAutoPickUpLoading = false
                                    )
                            }
                            else -> {
                                _autoPickUpPlayerEventChannel.send(
                                    UiEvent.ShowLongSnackbar(error.asUiText())
                                )

                                savedStateHandle["pickUpPlayerState"] =
                                    pickUpPlayerState.value.copy(
                                        isAutoPickUpLoading = false
                                    )
                            }
                        }
                    }
                    null -> {
                        savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                            isAutoPickUpLoading = false
                        )
                    }
                }
            } else {
                _autoPickUpPlayerEventChannel.send(
                    UiEvent.ShowOfflineSnackbar(
                        UiText.StringResource(R.string.error_network_connection_unavailable)
                    )
                )
            }
        }
    }

    private fun pickUpPlayerOnce() = viewModelScope.launch {
        if (NetworkObserverHelper.networkStatus == ConnectivityObserver.Status.AVAILABLE) {
            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                isPickUpOnceLoading = true
            )

            val pickUpPlayerResult = pickUpPlayerUseCases.pickUpPlayerUseCase.get()(
                minPrice = pickUpPlayerState.value.minPrice,
                maxPrice = pickUpPlayerState.value.maxPrice,
                takeAfterDelayInSeconds = pickUpPlayerState.value.takeAfterDelayInSeconds
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
                    PickUpPlayerError.InvalidMinPrice -> {
                        savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                            minPriceErrorText = error.asUiText()
                        )
                    }
                    else -> Unit
                }
            }

            if (pickUpPlayerResult.maxPriceError != null) {
                when (val error = pickUpPlayerResult.maxPriceError) {
                    PickUpPlayerError.InvalidMaxPrice -> {
                        savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                            maxPriceErrorText = error.asUiText()
                        )
                    }
                    else -> Unit
                }
            }

            if (pickUpPlayerResult.takeAfterError != null) {
                when (val error = pickUpPlayerResult.takeAfterError) {
                    PickUpPlayerError.InvalidTakeAfter -> {
                        savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                            takeAfterErrorText = error.asUiText()
                        )
                    }
                    else -> Unit
                }
            }

            when (val result = pickUpPlayerResult.result) {
                is Result.Success -> {
                    result.data.let { player ->
                        savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                            isPickUpOnceLoading = false
                        )

                        _pickUpPlayerOnceEventChannel.send(
                            PickUpPlayerUiEvent.NavigateToPickedUpPlayerInfoScreen(player)
                        )
                    }
                }
                is Result.Error -> {
                    when (val error = result.error) {
                        PickUpPlayerError.Network.DsfutSignature -> {
                            _pickUpPlayerOnceEventChannel.send(
                                PickUpPlayerUiEvent.ShowSignatureSnackbar(error.asUiText())
                            )

                            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                                isPickUpOnceLoading = false
                            )
                        }
                        else -> {
                            _pickUpPlayerOnceEventChannel.send(
                                UiEvent.ShowLongSnackbar(error.asUiText())
                            )

                            savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                                isPickUpOnceLoading = false
                            )
                        }
                    }
                }
                null -> {
                    savedStateHandle["pickUpPlayerState"] = pickUpPlayerState.value.copy(
                        isPickUpOnceLoading = false
                    )
                }
            }
        } else {
            _pickUpPlayerOnceEventChannel.send(
                UiEvent.ShowOfflineSnackbar(
                    UiText.StringResource(R.string.error_network_connection_unavailable)
                )
            )
        }
    }
}
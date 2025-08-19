package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.ui.navigation.screens.HistoryPlayerInfoScreen
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.common.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.PickedUpPlayerInfoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PickedUpPlayerInfoViewModel @Inject constructor(
    private val pickedUpPlayerInfoUseCases: PickedUpPlayerInfoUseCases,
    private val decimalFormat: DecimalFormat,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val player = pickedUpPlayerInfoUseCases.observePickedUpPlayerUseCase.get()(
        playerId = savedStateHandle.toRoute<HistoryPlayerInfoScreen>().playerId
    ).onEach { player ->
        startCountDownTimer(player.expiryTimeInMs)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = null
    )

    private val _timerText = MutableStateFlow<UiText>(
        UiText.StringResource(
            R.string.picked_up_player_info_timer,
            decimalFormat.format(0),
            decimalFormat.format(0)
        )
    )
    val timerText = _timerText.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = _timerText.value
    )

    private var countDownTimerJob: Job? = null

    override fun onCleared() {
        countDownTimerJob?.cancel()
        super.onCleared()
    }

    fun onAction(action: PickedUpPlayerInfoAction) {
        when (action) {
            is PickedUpPlayerInfoAction.StartCountDownTimer -> startCountDownTimer(action.expiryTimeInMs)
        }
    }

    private fun startCountDownTimer(
        expiryTimeInMs: Long
    ) {
        countDownTimerJob?.cancel()

        countDownTimerJob = pickedUpPlayerInfoUseCases.startCountDownTimerUseCase.get()(
            expiryTimeInMs = expiryTimeInMs
        ).onEach { timerValueInSeconds ->
            _timerText.update {
                val isTimerFinished = timerValueInSeconds == 0
                if (isTimerFinished) {
                    return@update UiText.StringResource(R.string.picked_up_player_info_timer_expired)
                }

                val minutes = decimalFormat.format(timerValueInSeconds / 60)
                val seconds = decimalFormat.format(timerValueInSeconds % 60)

                UiText.StringResource(
                    resId = R.string.picked_up_player_info_timer,
                    minutes,
                    seconds
                )
            }
        }.launchIn(scope = viewModelScope)
    }
}
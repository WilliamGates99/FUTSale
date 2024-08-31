package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.presentation.picked_up_player_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases.PickUpPlayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class PickedUpPlayerInfoViewModel @Inject constructor(
    private val pickUpPlayerUseCases: PickUpPlayerUseCases,
    private val decimalFormat: DecimalFormat
) : ViewModel() {

    private val _timerText = MutableStateFlow<UiText>(
        UiText.StringResource(
            R.string.picked_up_player_info_timer,
            decimalFormat.format(0),
            decimalFormat.format(0)
        )
    )
    val timerText = _timerText.asStateFlow()

    private var countDownTimerJob: Job? = null

    fun onAction(action: PickedUpPlayerInfoAction) {
        when (action) {
            is PickedUpPlayerInfoAction.StartCountDownTimer -> startCountDownTimer(action.expiryTimeInMs)
        }
    }

    private fun startCountDownTimer(expiryTimeInMs: Long) {
        countDownTimerJob?.cancel()
        countDownTimerJob = viewModelScope.launch {
            pickUpPlayerUseCases.startCountDownTimerUseCase.get()(expiryTimeInMs).collect { timerValueInSeconds ->
                _timerText.update {
                    val isTimerFinished = timerValueInSeconds == 0
                    if (isTimerFinished) {
                        UiText.StringResource(R.string.picked_up_player_info_timer_expired)
                    } else {
                        val minutes = decimalFormat.format(timerValueInSeconds / 60)
                        val seconds = decimalFormat.format(timerValueInSeconds % 60)

                        UiText.StringResource(
                            R.string.picked_up_player_info_timer,
                            minutes,
                            seconds
                        )
                    }
                }
            }
        }
    }
}
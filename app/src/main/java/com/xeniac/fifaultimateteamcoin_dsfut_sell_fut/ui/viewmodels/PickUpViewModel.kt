package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.DsfutRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PickUpViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val dsfutRepository: dagger.Lazy<DsfutRepository>
) : ViewModel() {

    private val _timerLiveData: MutableLiveData<Event<Long>> = MutableLiveData()
    val timerLiveData: LiveData<Event<Long>> = _timerLiveData

    var timerInMillis: Long = 0

    fun pickUpPlayer() = viewModelScope.launch {
        safePickUpPlayer()
    }

    private suspend fun safePickUpPlayer() {
        val expiresInMillis = 180 * 1000 // 180 Seconds
        startCountdown(expiresInMillis.toLong())
    }

    private fun startCountdown(startTimeInMillis: Long) {
        val countDownIntervalInMillis = 1000L // 1 Second

        object : CountDownTimer(startTimeInMillis, countDownIntervalInMillis) {
            override fun onTick(millisUntilFinished: Long) {
                timerInMillis = millisUntilFinished
                _timerLiveData.postValue(Event(millisUntilFinished))
                Timber.i("Timer remaining time: $millisUntilFinished")
            }

            override fun onFinish() {
                timerInMillis = 0
                _timerLiveData.postValue(Event(0))
            }
        }.start()
    }
}
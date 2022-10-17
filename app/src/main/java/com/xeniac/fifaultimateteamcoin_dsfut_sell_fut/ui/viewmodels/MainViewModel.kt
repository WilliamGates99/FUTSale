package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _isOnBoardingCompletedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isOnBoardingCompletedLiveData: LiveData<Boolean> = _isOnBoardingCompletedLiveData

    fun getIsOnBoardingCompleted() = viewModelScope.launch(Dispatchers.IO) {
        safeGetIsOnBoardingCompleted()
    }

    private suspend fun safeGetIsOnBoardingCompleted() {
        try {
            val isOnBoardingCompleted = preferencesRepository.isOnBoardingCompleted()
            _isOnBoardingCompletedLiveData.postValue(isOnBoardingCompleted)
            Timber.i("isOnBoardingCompleted = $isOnBoardingCompleted")
        } catch (e: Exception) {
            _isOnBoardingCompletedLiveData.postValue(false)
            Timber.e("safeGetIsOnBoardingCompleted Exception: ${e.message}")
        }
    }
}
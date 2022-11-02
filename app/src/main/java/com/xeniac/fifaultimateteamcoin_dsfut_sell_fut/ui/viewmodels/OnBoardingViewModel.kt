package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Resource
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _completeOnBoardingLiveData:
            MutableLiveData<Event<Resource<Nothing>>> = MutableLiveData()
    val completeOnBoardingLiveData: LiveData<Event<Resource<Nothing>>> = _completeOnBoardingLiveData

    fun completeOnBoarding(partnerId: String, secretKey: String) = viewModelScope.launch {
        safeCompleteOnBoarding(partnerId, secretKey)
    }

    private suspend fun safeCompleteOnBoarding(partnerId: String, secretKey: String) {
        _completeOnBoardingLiveData.postValue(Event(Resource.Loading()))
        try {
            preferencesRepository.setPartnerId(partnerId)
            preferencesRepository.setSecretKey(secretKey)
            preferencesRepository.isOnBoardingCompleted(true)
            _completeOnBoardingLiveData.postValue(Event(Resource.Success()))
            Timber.i("Profile data saved successfully.")
        } catch (e: Exception) {
            val message = UiText.DynamicString(e.message.toString())
            _completeOnBoardingLiveData.postValue(Event(Resource.Error(message)))
            Timber.e("safeCompleteOnBoarding Exception: ${message.value}")
        }
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_INPUT_BLANK_PARTNER_ID
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_INPUT_BLANK_SECRET_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _onBoardingLiveData: MutableLiveData<Event<Resource<Nothing>>> = MutableLiveData()
    val onBoardingLiveData: LiveData<Event<Resource<Nothing>>> = _onBoardingLiveData

    fun checkOnBoardingInputs(partnerId: String, secretKey: String) {
        if (partnerId.isBlank()) {
            _onBoardingLiveData.postValue(Event(Resource.error(ERROR_INPUT_BLANK_PARTNER_ID)))
            return
        }

        if (secretKey.isBlank()) {
            _onBoardingLiveData.postValue(Event(Resource.error(ERROR_INPUT_BLANK_SECRET_KEY)))
            return
        }

        completeOnBoarding(partnerId, secretKey)
    }

    fun completeOnBoarding(partnerId: String, secretKey: String) = viewModelScope.launch {
        safeCompleteOnBoarding(partnerId, secretKey)
    }

    private suspend fun safeCompleteOnBoarding(partnerId: String, secretKey: String) {
        _onBoardingLiveData.postValue(Event(Resource.loading()))
        try {
            preferencesRepository.setPartnerId(partnerId)
            preferencesRepository.setSecretKey(secretKey)
            preferencesRepository.isOnBoardingCompleted(true)
            _onBoardingLiveData.postValue(Event(Resource.success(null)))
            Timber.i("DSFUT data saved successfully.")
        } catch (e: Exception) {
            Timber.e("safeCompleteOnBoarding Exception: ${e.message}")
            _onBoardingLiveData.postValue(Event(Resource.error(e.message.toString())))
        }
    }
}
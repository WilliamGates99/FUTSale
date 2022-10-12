package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Resource
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _partnerIdLiveData: MutableLiveData<Event<String?>> = MutableLiveData()
    val partnerIdLiveData: LiveData<Event<String?>> = _partnerIdLiveData

    private val _secretKeyLiveData: MutableLiveData<Event<String?>> = MutableLiveData()
    val secretKeyLiveData: LiveData<Event<String?>> = _secretKeyLiveData

    private val _updatePartnerIdLiveData:
            MutableLiveData<Event<Resource<Nothing>>> = MutableLiveData()
    val updatePartnerIdLiveData: LiveData<Event<Resource<Nothing>>> = _updatePartnerIdLiveData

    private val _updateSecretKeyLiveData:
            MutableLiveData<Event<Resource<Nothing>>> = MutableLiveData()
    val updateSecretKeyLiveData: LiveData<Event<Resource<Nothing>>> = _updateSecretKeyLiveData

    fun getPartnerId() = viewModelScope.launch(Dispatchers.IO) {
        safeGetPartnerId()
    }

    private suspend fun safeGetPartnerId() {
        val partnerId = preferencesRepository.getPartnerId()
        _partnerIdLiveData.postValue(Event(partnerId))
        Timber.i("Partner ID is $partnerId")
    }

    fun getSecretKey() = viewModelScope.launch(Dispatchers.IO) {
        safeGetSecretKey()
    }

    private suspend fun safeGetSecretKey() {
        val secretKey = preferencesRepository.getSecretKey()
        _secretKeyLiveData.postValue(Event(secretKey))
        Timber.i("Secret key is $secretKey")
    }

    fun updatePartnerId(newPartnerId: Editable?) = viewModelScope.launch {
        safeUpdatePartnerId(newPartnerId)
    }

    private suspend fun safeUpdatePartnerId(newPartnerId: Editable?) {
        _updatePartnerIdLiveData.postValue(Event(Resource.loading()))
        try {
            if (newPartnerId.isNullOrBlank()) {
                preferencesRepository.setPartnerId(null)
                _updatePartnerIdLiveData.postValue(Event(Resource.success()))
            } else {
                val updatedPartnerId = newPartnerId.toString().trim()
                preferencesRepository.setPartnerId(updatedPartnerId)
                _updatePartnerIdLiveData.postValue(Event(Resource.success()))
            }
            Timber.i("PartnerId updated successfully.")
        } catch (e: Exception) {
            val message = UiText.StringResource(R.string.error_something_went_wrong)
            _updatePartnerIdLiveData.postValue(Event(Resource.error(message)))
            Timber.i("safeUpdatePartnerId Exception: ${e.message}")
        }
    }

    fun updateSecretKey(newSecretKey: Editable?) = viewModelScope.launch {
        safeUpdateSecretKey(newSecretKey)
    }

    private suspend fun safeUpdateSecretKey(newSecretKey: Editable?) {
        _updateSecretKeyLiveData.postValue(Event(Resource.loading()))
        try {
            if (newSecretKey.isNullOrBlank()) {
                preferencesRepository.setSecretKey(null)
                _updateSecretKeyLiveData.postValue(Event(Resource.success()))
            } else {
                val updatedSecretKey = newSecretKey.toString().trim()
                preferencesRepository.setSecretKey(updatedSecretKey)
                _updateSecretKeyLiveData.postValue(Event(Resource.success()))
            }
            Timber.i("SecretKey updated successfully.")
        } catch (e: Exception) {
            val message = UiText.StringResource(R.string.error_something_went_wrong)
            _updateSecretKeyLiveData.postValue(Event(Resource.error(message)))
            Timber.i("safeUpdateSecretKey Exception: ${e.message}")
        }
    }
}
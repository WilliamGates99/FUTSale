package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

/*
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

fun updatePartnerId(newPartnerId: String?) = viewModelScope.launch {
    safeUpdatePartnerId(newPartnerId)
}

private suspend fun safeUpdatePartnerId(newPartnerId: String?) {
    _updatePartnerIdLiveData.postValue(Event(Resource.Loading()))
    try {
        preferencesRepository.setPartnerId(newPartnerId)
        _updatePartnerIdLiveData.postValue(Event(Resource.Success()))
        Timber.i("PartnerId updated successfully.")
    } catch (e: Exception) {
        val message = UiText.StringResource(R.string.error_something_went_wrong)
        _updatePartnerIdLiveData.postValue(Event(Resource.Error(message)))
        Timber.i("safeUpdatePartnerId Exception: ${e.message}")
    }
}

fun updateSecretKey(newSecretKey: String?) = viewModelScope.launch {
    safeUpdateSecretKey(newSecretKey)
}

private suspend fun safeUpdateSecretKey(newSecretKey: String?) {
    _updateSecretKeyLiveData.postValue(Event(Resource.Loading()))
    try {
        preferencesRepository.setSecretKey(newSecretKey)
        _updateSecretKeyLiveData.postValue(Event(Resource.Success()))
        Timber.i("SecretKey updated successfully.")
    } catch (e: Exception) {
        val message = UiText.StringResource(R.string.error_something_went_wrong)
        _updateSecretKeyLiveData.postValue(Event(Resource.Error(message)))
        Timber.i("safeUpdateSecretKey Exception: ${e.message}")
    }
}
}
 */
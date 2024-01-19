package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

/*
@HiltViewModel
class MainViewModel @Inject constructor(
private val preferencesRepository: PreferencesRepository
) : ViewModel() {

private val _rateAppDialogChoiceLiveData: MutableLiveData<Event<Int>> = MutableLiveData()
val rateAppDialogChoiceLiveData: LiveData<Event<Int>> = _rateAppDialogChoiceLiveData

private val _previousRequestTimeInMillisLiveData:
        MutableLiveData<Event<Long>> = MutableLiveData()
val previousRequestTimeInMillisLiveData:
        LiveData<Event<Long>> = _previousRequestTimeInMillisLiveData

fun isOnBoardingCompleted() = preferencesRepository.isOnBoardingCompleted()

fun getRateAppDialogChoice() = viewModelScope.launch {
    safeGetRateAppDialogChoice()
}

private suspend fun safeGetRateAppDialogChoice() {
    _rateAppDialogChoiceLiveData.postValue(Event(preferencesRepository.getRateAppDialogChoice()))
}

fun getPreviousRequestTimeInMillis() = viewModelScope.launch {
    safeGetPreviousRequestTimeInMillis()
}

private suspend fun safeGetPreviousRequestTimeInMillis() {
    _previousRequestTimeInMillisLiveData.postValue(Event(preferencesRepository.getPreviousRequestTimeInMillis()))
}

fun setRateAppDialogChoice(value: Int) = viewModelScope.launch {
    safeSetRateAppDialogChoice(value)
}

private suspend fun safeSetRateAppDialogChoice(value: Int) {
    preferencesRepository.setRateAppDialogChoice(value)
    _rateAppDialogChoiceLiveData.postValue(Event(value))
}

fun setPreviousRequestTimeInMillis() = viewModelScope.launch {
    safeSetPreviousRequestTimeInMillis()
}

private suspend fun safeSetPreviousRequestTimeInMillis() {
    preferencesRepository.setPreviousRequestTimeInMillis(Calendar.getInstance().timeInMillis)
}
}
 */
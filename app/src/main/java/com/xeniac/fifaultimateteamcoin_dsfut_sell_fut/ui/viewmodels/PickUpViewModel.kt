package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.DsfutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PickUpViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val dsfutRepository: dagger.Lazy<DsfutRepository>,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    /*
    private val _selectedPlatformIndexLiveData: MutableLiveData<Event<String>> = MutableLiveData()
    val selectedPlatformIndexLiveData: LiveData<Event<String>> = _selectedPlatformIndexLiveData

    private val _pickPlayerOnceLiveData:
            MutableLiveData<Event<Resource<Player>>> = MutableLiveData()
    val pickPlayerOnceLiveData: LiveData<Event<Resource<Player>>> = _pickPlayerOnceLiveData

    private val _autoPickPlayerLiveData:
            MutableLiveData<Event<Resource<Player>>> = MutableLiveData()
    val autoPickPlayerLiveData: LiveData<Event<Resource<Player>>> = _autoPickPlayerLiveData

    private val _insertPickedUpPlayerLiveData:
            MutableLiveData<Event<Resource<PickedUpPlayer>>> = MutableLiveData()
    val insertPickedUpPlayerLiveData:
            LiveData<Event<Resource<PickedUpPlayer>>> = _insertPickedUpPlayerLiveData

    val allPickedUpPlayersLiveData = dsfutRepository.get().observeAllPickedUpPlayers()

    val pickPlayerExpiryTimerLiveData: LiveData<Long> =
        savedStateHandle.getLiveData<Long>("pickPlayerExpiryTimerLiveData")

    private val _pickedPlayerCardExpiryTimerLiveData:
            MutableLiveData<Event<Long>> = MutableLiveData()
    val pickedPlayerCardExpiryTimerLiveData:
            LiveData<Event<Long>> = _pickedPlayerCardExpiryTimerLiveData

    private var pickPlayerExpiryCountDownTimer: CountDownTimer? = null
    private var pickPlayerExpiryTimerInMillis: Long = 0

    private var pickedPlayerCardExpiryCountDownTimer: CountDownTimer? = null
    private var pickedPlayerCardExpiryTimerInMillis: Long = 0

    fun getSelectedPlatform() = viewModelScope.launch {
        safeGetSelectedPlatform()
    }

    private suspend fun safeGetSelectedPlatform() {
        val selectedPlatform = preferencesRepository.getSelectedPlatform()
        _selectedPlatformIndexLiveData.postValue(Event(selectedPlatform))
        Timber.i("Selected platform is $selectedPlatform")
    }

    fun setSelectedPlatform(platform: String) = viewModelScope.launch {
        safeSetSelectedPlatform(platform)
    }

    private suspend fun safeSetSelectedPlatform(checkedId: String) {
        preferencesRepository.setSelectedPlatform(checkedId)
        _selectedPlatformIndexLiveData.postValue(Event(checkedId))
        Timber.i("Selected platform changed to $checkedId")
    }

    fun validatePickOnceInputs(
        minPriceInput: String? = null,
        maxPriceInput: String? = null,
        takeAfterInput: String? = null
    ) = viewModelScope.launch {
        safeValidatePickOnceInputs(minPriceInput, maxPriceInput, takeAfterInput)
    }

    private suspend fun safeValidatePickOnceInputs(
        minPriceInput: String?, maxPriceInput: String?, takeAfterInput: String?
    ) {
        val partnerId = preferencesRepository.getPartnerId()
        if (partnerId.isNullOrBlank()) {
            _pickPlayerOnceLiveData.postValue(
                Event(Resource.Error(UiText.StringResource(R.string.pick_up_error_blank_partner_id)))
            )
            return
        }

        val secretKey = preferencesRepository.getSecretKey()
        if (secretKey.isNullOrBlank()) {
            _pickPlayerOnceLiveData.postValue(
                Event(Resource.Error(UiText.StringResource(R.string.pick_up_error_blank_secret_key)))
            )
            return
        }

        var minPrice: Int? = null
        var maxPrice: Int? = null
        var takeAfter: Int? = null

        if (!minPriceInput.isNullOrBlank()) {
            minPrice = minPriceInput.toInt()
        }

        if (!maxPriceInput.isNullOrBlank()) {
            maxPrice = maxPriceInput.toInt()
        }

        if (!takeAfterInput.isNullOrBlank()) {
            takeAfter = takeAfterInput.toInt()
        }

        pickPlayerOnce(partnerId, secretKey, minPrice, maxPrice, takeAfter)
    }

    private fun pickPlayerOnce(
        partnerId: String,
        secretKey: String,
        minPrice: Int?,
        maxPrice: Int?,
        takeAfter: Int?
    ) = viewModelScope.launch {
        _pickPlayerOnceLiveData.postValue(Event(Resource.Loading()))

        val platform = preferencesRepository.getSelectedPlatform()
        val response = dsfutRepository.get().pickUpPlayer(
            platform, partnerId, secretKey, minPrice, maxPrice, takeAfter
        )

        _pickPlayerOnceLiveData.postValue(Event(response))
    }

    fun validateAutoPickPlayerInputs(
        minPriceInput: String? = null,
        maxPriceInput: String? = null,
        takeAfterInput: String? = null
    ) = viewModelScope.launch {
        safeValidateAutoPickPlayerInputs(minPriceInput, maxPriceInput, takeAfterInput)
    }

    private suspend fun safeValidateAutoPickPlayerInputs(
        minPriceInput: String?, maxPriceInput: String?, takeAfterInput: String?
    ) {
        val partnerId = preferencesRepository.getPartnerId()
        if (partnerId.isNullOrBlank()) {
            _autoPickPlayerLiveData.postValue(
                Event(Resource.Error(UiText.StringResource(R.string.pick_up_error_blank_partner_id)))
            )
            return
        }

        val secretKey = preferencesRepository.getSecretKey()
        if (secretKey.isNullOrBlank()) {
            _autoPickPlayerLiveData.postValue(
                Event(Resource.Error(UiText.StringResource(R.string.pick_up_error_blank_secret_key)))
            )
            return
        }

        var minPrice: Int? = null
        var maxPrice: Int? = null
        var takeAfter: Int? = null

        if (!minPriceInput.isNullOrBlank()) {
            minPrice = minPriceInput.toInt()
        }

        if (!maxPriceInput.isNullOrBlank()) {
            maxPrice = maxPriceInput.toInt()
        }

        if (!takeAfterInput.isNullOrBlank()) {
            takeAfter = takeAfterInput.toInt()
        }

        autoPickPlayer(partnerId, secretKey, minPrice, maxPrice, takeAfter)
    }

    private fun autoPickPlayer(
        partnerId: String,
        secretKey: String,
        minPrice: Int?,
        maxPrice: Int?,
        takeAfter: Int?
    ) = viewModelScope.launch {
        safeAutoPickPlayer(partnerId, secretKey, minPrice, maxPrice, takeAfter)
    }

    private suspend fun safeAutoPickPlayer(
        partnerId: String, secretKey: String, minPrice: Int?, maxPrice: Int?, takeAfter: Int?
    ) {
        _autoPickPlayerLiveData.postValue(Event(Resource.Loading()))
        try {
            delay(DELAY_TIME_AUTO_PICK_UP)

            val platform = preferencesRepository.getSelectedPlatform()
            val response = dsfutRepository.get().pickUpPlayer(
                platform, partnerId, secretKey, minPrice, maxPrice, takeAfter
            )

            _autoPickPlayerLiveData.postValue(Event(response))
        } catch (e: Exception) {
            _autoPickPlayerLiveData.postValue(Event(Resource.Error(UiText.DynamicString(e.message.toString()))))
            Timber.e("safeAutoPickPlayer exception: ${e.message}")
        }
    }

    fun cancelAutoPickPlayer() = viewModelScope.launch {
        safeCancelAutoPickPlayer()
    }

    private fun safeCancelAutoPickPlayer() {
        try {
            _autoPickPlayerLiveData.postValue(
                Event(Resource.Error(UiText.StringResource(R.string.pick_up_error_dsfut_canceled)))
            )
            Timber.i("Auto pick player canceled.")
        } catch (e: Exception) {
            _autoPickPlayerLiveData.postValue(Event(Resource.Error(UiText.DynamicString(e.message.toString()))))
            Timber.e("safeCancelAutoPickPlayer exception: ${e.message}")
        }
    }

    fun insertPickedUpPlayerIntoDb(player: Player) = viewModelScope.launch {
        try {
            val pickedUpPlayer = PickedUpPlayer(
                name = player.name,
                position = player.position,
                rating = player.rating,
                priceStart = player.startPrice,
                priceNow = player.buyNowPrice
            )
            dsfutRepository.get().insertPickedUpPlayer(pickedUpPlayer)
            _insertPickedUpPlayerLiveData.postValue(Event(Resource.Success(pickedUpPlayer)))
            Timber.i("Picked Player: $pickedUpPlayer")
            Timber.i("Picked Player inserted into database.")
        } catch (e: Exception) {
            val message = UiText.DynamicString(e.message.toString())
            _insertPickedUpPlayerLiveData.postValue(Event(Resource.Error(message)))
            Timber.e("insertPickedUpPlayerIntoDb Exception: ${message.value}")
        }
    }

    fun startPickPlayerExpiryCountdown() {
        pickPlayerExpiryCountDownTimer?.cancel()

        pickPlayerExpiryCountDownTimer =
            object : CountDownTimer(PLAYER_EXPIRY_TIME_IN_MILLIS, COUNT_DOWN_INTERVAL_IN_MILLIS) {
                override fun onTick(millisUntilFinished: Long) {
                    pickPlayerExpiryTimerInMillis = millisUntilFinished
                    savedStateHandle["pickPlayerExpiryTimerLiveData"] = millisUntilFinished
                    Timber.i("pickPlayerExpiryCountDownTimer remaining time: $millisUntilFinished")
                }

                override fun onFinish() {
                    pickPlayerExpiryTimerInMillis = 0
                    savedStateHandle["pickPlayerExpiryTimerLiveData"] = 0L
                    Timber.i("pickPlayerExpiryCountDownTimer finished.")
                }
            }.start()
    }

    fun startPickedPlayerCardExpiryCountdown(startTimeInMillis: Long) {
        pickedPlayerCardExpiryCountDownTimer?.cancel()

        pickedPlayerCardExpiryCountDownTimer =
            object : CountDownTimer(startTimeInMillis, COUNT_DOWN_INTERVAL_IN_MILLIS) {
                override fun onTick(millisUntilFinished: Long) {
                    pickedPlayerCardExpiryTimerInMillis = millisUntilFinished
                    _pickedPlayerCardExpiryTimerLiveData.postValue(Event(millisUntilFinished))
                    Timber.i("pickedPlayerCardExpiryCountDownTimer remaining time: $millisUntilFinished")
                }

                override fun onFinish() {
                    pickedPlayerCardExpiryTimerInMillis = 0
                    _pickedPlayerCardExpiryTimerLiveData.postValue(Event(0))
                    Timber.i("pickedPlayerCardExpiryCountDownTimer finished.")
                }
            }.start()
    }

     */
}
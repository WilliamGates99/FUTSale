package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local.models.PickedUpPlayer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.DsfutRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.COUNT_DOWN_INTERVAL_IN_MILLIS
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DELAY_TIME_AUTO_PICK_UP
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_DSFUT_BLOCK
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_DSFUT_EMPTY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_DSFUT_LIMIT
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_DSFUT_MAINTENANCE
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_DSFUT_SIGN
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_DSFUT_THROTTLE
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.PLAYER_EXPIRY_TIME_IN_MILLIS
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.DateHelper.getCurrentTimeInMillis
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Resource
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class PickUpViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val dsfutRepository: dagger.Lazy<DsfutRepository>
) : ViewModel() {

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

    private val _pickPlayerExpiryTimerLiveData: MutableLiveData<Event<Long>> = MutableLiveData()
    val pickPlayerExpiryTimerLiveData: LiveData<Event<Long>> = _pickPlayerExpiryTimerLiveData

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
        minPriceInput: String?, maxPriceInput: String?, takeAfterInput: String?
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
        safePickPlayerOnce(partnerId, secretKey, minPrice, maxPrice, takeAfter)
    }

    private suspend fun safePickPlayerOnce(
        partnerId: String, secretKey: String, minPrice: Int?, maxPrice: Int?, takeAfter: Int?
    ) {
        _pickPlayerOnceLiveData.postValue(Event(Resource.Loading()))
        try {
            val platform = preferencesRepository.getSelectedPlatform()
            val timestamp = getCurrentTimeInMillis()
            val signature = getMd5Signature(partnerId, secretKey, timestamp)

            val response = dsfutRepository.get().pickUpPlayer(
                platform, partnerId, timestamp, signature, minPrice, maxPrice, takeAfter
            )

            response.body()?.let {
                it.error?.let { error -> // RESPONSE HAD ERROR
                    when {
                        error.contains(ERROR_DSFUT_BLOCK) -> {
                            val errorMessage = "$error - ${it.message}"
                            _pickPlayerOnceLiveData.postValue(
                                Event(Resource.Error(UiText.DynamicString(errorMessage)))
                            )
                        }
                        error.contains(ERROR_DSFUT_EMPTY) -> _pickPlayerOnceLiveData.postValue(
                            Event(Resource.Error(UiText.StringResource(R.string.pick_up_error_dsfut_empty)))
                        )
                        error.contains(ERROR_DSFUT_LIMIT) -> _pickPlayerOnceLiveData.postValue(
                            Event(Resource.Error(UiText.StringResource(R.string.pick_up_error_dsfut_limit)))
                        )
                        error.contains(ERROR_DSFUT_MAINTENANCE) -> _pickPlayerOnceLiveData.postValue(
                            Event(Resource.Error(UiText.StringResource(R.string.pick_up_error_dsfut_maintenance)))
                        )
                        error.contains(ERROR_DSFUT_SIGN) -> _pickPlayerOnceLiveData.postValue(
                            Event(Resource.Error(UiText.StringResource(R.string.pick_up_error_dsfut_sign)))
                        )
                        error.contains(ERROR_DSFUT_THROTTLE) -> _pickPlayerOnceLiveData.postValue(
                            Event(Resource.Error(UiText.StringResource(R.string.pick_up_error_dsfut_throttle)))
                        )
                        else -> _pickPlayerOnceLiveData.postValue(
                            Event(Resource.Error(UiText.StringResource(R.string.error_something_went_wrong)))
                        )
                    }
                    Timber.e("safePickPlayerOnce error: ${it.error}")
                    Timber.e("safePickPlayerOnce message: ${it.message}")
                }

                it.player?.let { player -> // RESPONSE WAS SUCCESSFUL
                    startPickPlayerExpiryCountdown()
                    insertPickedUpPlayerIntoDb(player)
                    _pickPlayerOnceLiveData.postValue(Event(Resource.Success(player)))
                    Timber.i("safePickPlayerOnce: ${it.message}")
                }
            }
        } catch (e: Exception) {
            _pickPlayerOnceLiveData.postValue(Event(Resource.Error(UiText.DynamicString(e.message.toString()))))
            Timber.e("safePickPlayerOnce exception: ${e.message}")
        }
    }

    fun validateAutoPickPlayerInputs(
        minPriceInput: String?, maxPriceInput: String?, takeAfterInput: String?
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
            val timestamp = getCurrentTimeInMillis()
            val signature = getMd5Signature(partnerId, secretKey, timestamp)

            val response = dsfutRepository.get().pickUpPlayer(
                platform, partnerId, timestamp, signature, minPrice, maxPrice, takeAfter
            )

            response.body()?.let {
                it.error?.let { error -> // RESPONSE HAD ERROR
                    when {
                        error.contains(ERROR_DSFUT_BLOCK) -> {
                            val errorMessage = "$error - ${it.message}"
                            _autoPickPlayerLiveData.postValue(
                                Event(Resource.Error(UiText.DynamicString(errorMessage)))
                            )
                        }
                        error.contains(ERROR_DSFUT_EMPTY) -> _autoPickPlayerLiveData.postValue(
                            Event(Resource.Error(UiText.StringResource(R.string.pick_up_error_dsfut_empty)))
                        )
                        error.contains(ERROR_DSFUT_LIMIT) -> _autoPickPlayerLiveData.postValue(
                            Event(Resource.Error(UiText.StringResource(R.string.pick_up_error_dsfut_limit)))
                        )
                        error.contains(ERROR_DSFUT_MAINTENANCE) -> _autoPickPlayerLiveData.postValue(
                            Event(Resource.Error(UiText.StringResource(R.string.pick_up_error_dsfut_maintenance)))
                        )
                        error.contains(ERROR_DSFUT_SIGN) -> _pickPlayerOnceLiveData.postValue(
                            Event(Resource.Error(UiText.StringResource(R.string.pick_up_error_dsfut_sign)))
                        )
                        error.contains(ERROR_DSFUT_THROTTLE) -> _autoPickPlayerLiveData.postValue(
                            Event(Resource.Error(UiText.StringResource(R.string.pick_up_error_dsfut_throttle)))
                        )
                        else -> _autoPickPlayerLiveData.postValue(
                            Event(Resource.Error(UiText.StringResource(R.string.error_something_went_wrong)))
                        )
                    }
                    Timber.e("safeAutoPickPlayer error: ${it.error}")
                    Timber.e("safeAutoPickPlayer message: ${it.message}")
                }

                it.player?.let { player -> // RESPONSE WAS SUCCESSFUL
                    startPickPlayerExpiryCountdown()
                    insertPickedUpPlayerIntoDb(player)
                    _autoPickPlayerLiveData.postValue(Event(Resource.Success(player)))
                    Timber.i("safeAutoPickPlayer: ${it.message}")
                }
            }
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

    private fun startPickPlayerExpiryCountdown() {
        pickPlayerExpiryCountDownTimer?.cancel()

        pickPlayerExpiryCountDownTimer =
            object : CountDownTimer(PLAYER_EXPIRY_TIME_IN_MILLIS, COUNT_DOWN_INTERVAL_IN_MILLIS) {
                override fun onTick(millisUntilFinished: Long) {
                    pickPlayerExpiryTimerInMillis = millisUntilFinished
                    _pickPlayerExpiryTimerLiveData.postValue(Event(millisUntilFinished))
                    Timber.i("pickPlayerExpiryCountDownTimer remaining time: $millisUntilFinished")
                }

                override fun onFinish() {
                    pickPlayerExpiryTimerInMillis = 0
                    _pickPlayerExpiryTimerLiveData.postValue(Event(0))
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

    private fun getMd5Signature(partnerId: String, secretKey: String, timestamp: String): String {
        val input = partnerId + secretKey + timestamp
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(23, '0')
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.DsfutResponse
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.DsfutRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DELAY_TIME_AUTO_PICK_UP
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_DSFUT_BLOCK
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_DSFUT_EMPTY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_DSFUT_LIMIT
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_DSFUT_MAINTENANCE
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.ERROR_DSFUT_THROTTLE
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Resource
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PickUpViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val dsfutRepository: dagger.Lazy<DsfutRepository>
) : ViewModel() {

    private val _selectedPlatformIndexLiveData: MutableLiveData<Event<String>> = MutableLiveData()
    val selectedPlatformIndexLiveData: LiveData<Event<String>> = _selectedPlatformIndexLiveData

    private val _pickPlayerOnceLiveData:
            MutableLiveData<Event<Resource<DsfutResponse>>> = MutableLiveData()
    val pickPlayerOnceLiveData: LiveData<Event<Resource<DsfutResponse>>> = _pickPlayerOnceLiveData

    private val _autoPickPlayerLiveData:
            MutableLiveData<Event<Resource<DsfutResponse>>> = MutableLiveData()
    val autoPickPlayerLiveData: LiveData<Event<Resource<DsfutResponse>>> = _autoPickPlayerLiveData

    private val _isPlayerPickedUpLiveData: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isPlayerPickedUpLiveData: LiveData<Event<Boolean>> = _isPlayerPickedUpLiveData

    private val _pickedUpPlayerLiveData: MutableLiveData<Event<Player>> = MutableLiveData()
    val pickedUpPlayerLiveData: LiveData<Event<Player>> = _pickedUpPlayerLiveData

    private val _timerLiveData: MutableLiveData<Event<Long>> = MutableLiveData()
    val timerLiveData: LiveData<Event<Long>> = _timerLiveData

    var timerInMillis: Long = 0

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
                Event(Resource.error(UiText.StringResource(R.string.pick_up_error_blank_partner_id)))
            )
            return
        }

        val secretKey = preferencesRepository.getSecretKey()
        if (secretKey.isNullOrBlank()) {
            _pickPlayerOnceLiveData.postValue(
                Event(Resource.error(UiText.StringResource(R.string.pick_up_error_blank_secret_key)))
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
        _pickPlayerOnceLiveData.postValue(Event(Resource.loading()))
        try {
            val platform = preferencesRepository.getSelectedPlatform()
            val timestamp = getCurrentTime()
            val signature = getMd5Signature(partnerId, secretKey, timestamp)

            val response = dsfutRepository.get().pickUpPlayer(
                platform, partnerId, timestamp, signature, minPrice, maxPrice, takeAfter
            )

            // TODO TEST WITH DUMMY DATA

            response.body()?.let {
                when {
                    it.error.isNotBlank() -> { // RESPONSE HAD ERROR
                        when {
                            it.error.contains(ERROR_DSFUT_BLOCK) -> _pickPlayerOnceLiveData.postValue(
                                Event(Resource.error(UiText.StringResource(R.string.pick_up_error_dsfut_block)))
                            )
                            it.error.contains(ERROR_DSFUT_EMPTY) -> _pickPlayerOnceLiveData.postValue(
                                Event(Resource.error(UiText.StringResource(R.string.pick_up_error_dsfut_empty)))
                            )
                            it.error.contains(ERROR_DSFUT_LIMIT) -> _pickPlayerOnceLiveData.postValue(
                                Event(Resource.error(UiText.StringResource(R.string.pick_up_error_dsfut_limit)))
                            )
                            it.error.contains(ERROR_DSFUT_MAINTENANCE) -> _pickPlayerOnceLiveData.postValue(
                                Event(Resource.error(UiText.StringResource(R.string.pick_up_error_dsfut_maintenance)))
                            )
                            it.error.contains(ERROR_DSFUT_THROTTLE) -> _pickPlayerOnceLiveData.postValue(
                                Event(Resource.error(UiText.StringResource(R.string.pick_up_error_dsfut_throttle)))
                            )
                            else -> _pickPlayerOnceLiveData.postValue(
                                Event(Resource.error(UiText.StringResource(R.string.error_something_went_wrong)))
                            )
                        }
                        Timber.e("safePickPlayerOnce error: ${it.error}")
                    }
                    else -> { // RESPONSE WAS SUCCESSFUL
                        val expiresInMillis = it.player.expires
                        startCountdown(expiresInMillis.toLong())
                        _pickPlayerOnceLiveData.postValue(Event(Resource.success(it)))
                        _isPlayerPickedUpLiveData.postValue(Event(true))
                        Timber.i("safePickPlayerOnce: ${it.message}")
                    }
                }
            }
        } catch (e: Exception) {
            _pickPlayerOnceLiveData.postValue(Event(Resource.error(UiText.DynamicString(e.message.toString()))))
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
                Event(Resource.error(UiText.StringResource(R.string.pick_up_error_blank_partner_id)))
            )
            return
        }

        val secretKey = preferencesRepository.getSecretKey()
        if (secretKey.isNullOrBlank()) {
            _autoPickPlayerLiveData.postValue(
                Event(Resource.error(UiText.StringResource(R.string.pick_up_error_blank_secret_key)))
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
        _autoPickPlayerLiveData.postValue(Event(Resource.loading()))
        try {
            delay(DELAY_TIME_AUTO_PICK_UP)
            val platform = preferencesRepository.getSelectedPlatform()
            val timestamp = getCurrentTime()
            val signature = getMd5Signature(partnerId, secretKey, timestamp)

            val response = dsfutRepository.get().pickUpPlayer(
                platform, partnerId, timestamp, signature, minPrice, maxPrice, takeAfter
            )

            // TODO TEST WITH DUMMY DATA

            response.body()?.let {
                when {
                    it.error.isNotBlank() -> { // RESPONSE HAD ERROR
                        when {
                            it.error.contains(ERROR_DSFUT_BLOCK) -> _autoPickPlayerLiveData.postValue(
                                Event(Resource.error(UiText.StringResource(R.string.pick_up_error_dsfut_block)))
                            )
                            it.error.contains(ERROR_DSFUT_EMPTY) -> _autoPickPlayerLiveData.postValue(
                                Event(Resource.error(UiText.StringResource(R.string.pick_up_error_dsfut_empty)))
                            )
                            it.error.contains(ERROR_DSFUT_LIMIT) -> _autoPickPlayerLiveData.postValue(
                                Event(Resource.error(UiText.StringResource(R.string.pick_up_error_dsfut_limit)))
                            )
                            it.error.contains(ERROR_DSFUT_MAINTENANCE) -> _autoPickPlayerLiveData.postValue(
                                Event(Resource.error(UiText.StringResource(R.string.pick_up_error_dsfut_maintenance)))
                            )
                            it.error.contains(ERROR_DSFUT_THROTTLE) -> _autoPickPlayerLiveData.postValue(
                                Event(Resource.error(UiText.StringResource(R.string.pick_up_error_dsfut_throttle)))
                            )
                            else -> _autoPickPlayerLiveData.postValue(
                                Event(Resource.error(UiText.StringResource(R.string.error_something_went_wrong)))
                            )
                        }
                        Timber.e("safeAutoPickPlayer error: ${it.error}")
                    }
                    else -> { // RESPONSE WAS SUCCESSFUL
                        val expiresInMillis = it.player.expires
                        startCountdown(expiresInMillis.toLong())
                        _autoPickPlayerLiveData.postValue(Event(Resource.success(it)))
                        _isPlayerPickedUpLiveData.postValue(Event(true))
                        Timber.i("safeAutoPickPlayer: ${it.message}")
                    }
                }
            }
        } catch (e: Exception) {
            _autoPickPlayerLiveData.postValue(Event(Resource.error(UiText.DynamicString(e.message.toString()))))
            Timber.e("safeAutoPickPlayer exception: ${e.message}")
        }
    }

    fun cancelAutoPickPlayer() = viewModelScope.launch {
        safeCancelAutoPickPlayer()
    }

    private fun safeCancelAutoPickPlayer() {
        try {
            _autoPickPlayerLiveData.postValue(
                Event(Resource.error(UiText.StringResource(R.string.pick_up_error_dsfut_canceled)))
            )
        } catch (e: Exception) {
            _autoPickPlayerLiveData.postValue(Event(Resource.error(UiText.DynamicString(e.message.toString()))))
            Timber.e("safeCancelAutoPickPlayer exception: ${e.message}")
        }
    }

    fun getIsPlayerPickedUp() = viewModelScope.launch {
        safeGetIsPlayerPickedUp()
    }

    private suspend fun safeGetIsPlayerPickedUp() {

    }

    fun getPickedUpPlayer() = viewModelScope.launch {
        safeGetPickedUpPlayer()
    }

    private suspend fun safeGetPickedUpPlayer() {

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
                _isPlayerPickedUpLiveData.postValue(Event(false))
                _timerLiveData.postValue(Event(0))
            }
        }.start()
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        return calendar.timeInMillis.toString()
    }

    private fun getMd5Signature(partnerId: String, secretKey: String, timestamp: String): String {
        val input = partnerId + secretKey + timestamp
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(23, '0')
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.LOCALE_ENGLISH_GREAT_BRITAIN
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.LOCALE_ENGLISH_UNITED_STATES
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.LOCALE_PERSIAN_IRAN
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Event
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.SettingsHelper.setAppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _currentLanguageLiveData: MutableLiveData<Event<UiText>> = MutableLiveData()
    val currentLanguageLiveData: LiveData<Event<UiText>> = _currentLanguageLiveData

    private val _currentLocaleIndexLiveData: MutableLiveData<Event<Int>> = MutableLiveData()
    val currentLocaleIndexLiveData: LiveData<Event<Int>> = _currentLocaleIndexLiveData

    private val _currentThemeLiveData: MutableLiveData<Event<UiText>> = MutableLiveData()
    val currentThemeLiveData: LiveData<Event<UiText>> = _currentThemeLiveData

    private val _currentThemeIndexLiveData: MutableLiveData<Event<Int>> = MutableLiveData()
    val currentThemeIndexLiveData: LiveData<Event<Int>> = _currentThemeIndexLiveData

    private val _isNotificationSoundActiveLiveData:
            MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isNotificationSoundActiveLiveData:
            LiveData<Event<Boolean>> = _isNotificationSoundActiveLiveData

    private val _isNotificationVibrateActiveLiveData:
            MutableLiveData<Event<Boolean>> = MutableLiveData()
    val isNotificationVibrateActiveLiveData:
            LiveData<Event<Boolean>> = _isNotificationVibrateActiveLiveData

    private val _changeCurrentLocaleLiveData: MutableLiveData<Event<Int>> = MutableLiveData()
    val changeCurrentLocaleLiveData: LiveData<Event<Int>> = _changeCurrentLocaleLiveData

    fun getCurrentLanguage() = viewModelScope.launch {
        safeGetCurrentLanguage()
    }

    private fun safeGetCurrentLanguage() {
        val localeList = AppCompatDelegate.getApplicationLocales()

        if (localeList.isEmpty) {
            _currentLanguageLiveData.postValue(
                Event(UiText.StringResource(R.string.settings_text_settings_language_default))
            )
            _currentLocaleIndexLiveData.postValue(Event(0))
            Timber.i("Current language is System Default")
            Timber.i("Current locale index is 0")
        } else {
            val localeString = localeList[0].toString()
            Timber.i("Current language is $localeString")

            when (localeString) {
                "en_US" -> {
                    _currentLanguageLiveData.postValue(
                        Event(UiText.StringResource(R.string.settings_text_settings_language_english_us))
                    )
                    _currentLocaleIndexLiveData.postValue(Event(1))
                    Timber.i("Current locale index is 1")
                }
                "en_GB" -> {
                    _currentLanguageLiveData.postValue(
                        Event(UiText.StringResource(R.string.settings_text_settings_language_english_gb))
                    )
                    _currentLocaleIndexLiveData.postValue(Event(2))
                    Timber.i("Current locale index is 2")
                }
                "fa_IR" -> {
                    _currentLanguageLiveData.postValue(
                        Event(UiText.StringResource(R.string.settings_text_settings_language_persian_ir))
                    )
                    _currentLocaleIndexLiveData.postValue(Event(3))
                    Timber.i("Current locale index is 3")
                }
                else -> {
                    _currentLanguageLiveData.postValue(
                        Event(UiText.StringResource(R.string.settings_text_settings_language_default))
                    )
                    _currentLocaleIndexLiveData.postValue(Event(0))
                    Timber.i("Current locale index is 0")
                }
            }
        }
    }

    fun getCurrentTheme() = viewModelScope.launch {
        safeGetCurrentTheme()
    }

    private suspend fun safeGetCurrentTheme() {
        val currentThemeIndex = preferencesRepository.getCurrentAppTheme()

        when (currentThemeIndex) {
            0 -> _currentThemeLiveData.postValue(
                Event(UiText.StringResource(R.string.settings_text_settings_theme_default))
            )
            1 -> _currentThemeLiveData.postValue(
                Event(UiText.StringResource(R.string.settings_text_settings_theme_light))
            )
            2 -> _currentThemeLiveData.postValue(
                Event(UiText.StringResource(R.string.settings_text_settings_theme_dark))
            )
        }

        _currentThemeIndexLiveData.postValue(Event(currentThemeIndex))

        Timber.i("Current theme index is $currentThemeIndex")
    }

    fun getIsNotificationSoundActive() = viewModelScope.launch {
        safeGetIsNotificationSoundActive()
    }

    private suspend fun safeGetIsNotificationSoundActive() {
        val isActive = preferencesRepository.isNotificationSoundActive()
        _isNotificationSoundActiveLiveData.postValue(Event(isActive))
        Timber.i("isNotificationSoundActive = $isActive")
    }

    fun getIsNotificationVibrateActive() = viewModelScope.launch {
        safeGetIsNotificationVibrateActive()
    }

    private suspend fun safeGetIsNotificationVibrateActive() {
        val isActive = preferencesRepository.isNotificationVibrateActive()
        _isNotificationVibrateActiveLiveData.postValue(Event(isActive))
        Timber.i("isNotificationVibrateActive = $isActive")
    }

    fun changeCurrentLocale(index: Int) = viewModelScope.launch {
        safeChangeCurrentLocale(index)
    }

    private fun safeChangeCurrentLocale(index: Int) {
        when (index) {
            // TODO DEFAULT LANGUAGE HAS ISSUE
            0 -> AppCompatDelegate.setApplicationLocales(LocaleListCompat.getAdjustedDefault())
            1 -> AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(LOCALE_ENGLISH_UNITED_STATES)
            )
            2 -> AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(LOCALE_ENGLISH_GREAT_BRITAIN)
            )
            3 -> AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(LOCALE_PERSIAN_IRAN)
            )
        }
        _changeCurrentLocaleLiveData.postValue(Event(index))
        Timber.i("App locale index changed to $index")
    }

    fun changeCurrentTheme(index: Int) = viewModelScope.launch {
        safeChangeCurrentTheme(index)
    }

    private suspend fun safeChangeCurrentTheme(index: Int) {
        preferencesRepository.setCurrentAppTheme(index)
        setAppTheme(index)
        getCurrentTheme()
        Timber.i("App theme index changed to $index")
    }
}
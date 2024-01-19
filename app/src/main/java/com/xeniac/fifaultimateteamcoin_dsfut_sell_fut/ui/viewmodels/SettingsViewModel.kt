package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

/*
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

private val _changeCurrentLocaleLiveData: MutableLiveData<Event<Boolean>> = MutableLiveData()
val changeCurrentLocaleLiveData: LiveData<Event<Boolean>> = _changeCurrentLocaleLiveData

private val _changeIsNotificationSoundActiveLiveData:
        MutableLiveData<Event<Resource<Boolean>>> = MutableLiveData()
val changeIsNotificationSoundActiveLiveData:
        LiveData<Event<Resource<Boolean>>> = _changeIsNotificationSoundActiveLiveData

private val _changeIsNotificationVibrateActiveLiveData:
        MutableLiveData<Event<Resource<Boolean>>> = MutableLiveData()
val changeIsNotificationVibrateActiveLiveData:
        LiveData<Event<Resource<Boolean>>> = _changeIsNotificationVibrateActiveLiveData

fun getCurrentLanguage() = viewModelScope.launch {
    safeGetCurrentLanguage()
}

private fun safeGetCurrentLanguage() {
    val localeList = AppCompatDelegate.getApplicationLocales()

    if (localeList.isEmpty) {
        _currentLanguageLiveData.postValue(
            Event(UiText.StringResource(R.string.settings_text_settings_language_default))
        )
        _currentLocaleIndexLiveData.postValue(Event(LOCALE_INDEX_DEFAULT))
        Timber.i("Current language is System Default")
        Timber.i("Current locale index is $LOCALE_INDEX_DEFAULT")
    } else {
        val localeString = localeList[0].toString()
        Timber.i("Current language is $localeString")

        when (localeString) {
            "en_US" -> {
                _currentLanguageLiveData.postValue(
                    Event(UiText.StringResource(R.string.settings_text_settings_language_english_us))
                )
                _currentLocaleIndexLiveData.postValue(Event(LOCALE_INDEX_ENGLISH_UNITED_STATES))
                Timber.i("Current locale index is $LOCALE_INDEX_ENGLISH_UNITED_STATES")
            }
            "en_GB" -> {
                _currentLanguageLiveData.postValue(
                    Event(UiText.StringResource(R.string.settings_text_settings_language_english_gb))
                )
                _currentLocaleIndexLiveData.postValue(Event(LOCALE_INDEX_ENGLISH_GREAT_BRITAIN))
                Timber.i("Current locale index is $LOCALE_INDEX_ENGLISH_GREAT_BRITAIN")
            }
            "fa_IR" -> {
                _currentLanguageLiveData.postValue(
                    Event(UiText.StringResource(R.string.settings_text_settings_language_persian_ir))
                )
                _currentLocaleIndexLiveData.postValue(Event(LOCALE_INDEX_PERSIAN_IRAN))
                Timber.i("Current locale index is $LOCALE_INDEX_PERSIAN_IRAN")
            }
            else -> {
                _currentLanguageLiveData.postValue(
                    Event(UiText.StringResource(R.string.settings_text_settings_language_default))
                )
                _currentLocaleIndexLiveData.postValue(Event(LOCALE_INDEX_DEFAULT))
                Timber.i("Current locale index is $LOCALE_INDEX_DEFAULT")
            }
        }
    }
}

fun getCurrentTheme() = viewModelScope.launch {
    safeGetCurrentTheme()
}

private suspend fun safeGetCurrentTheme() {
    val currentThemeIndex = preferencesRepository.getCurrentAppThemeIndex()

    when (currentThemeIndex) {
        THEME_INDEX_DEFAULT -> _currentThemeLiveData.postValue(
            Event(UiText.StringResource(R.string.settings_text_settings_theme_default))
        )
        THEME_INDEX_LIGHT -> _currentThemeLiveData.postValue(
            Event(UiText.StringResource(R.string.settings_text_settings_theme_light))
        )
        THEME_INDEX_DARK -> _currentThemeLiveData.postValue(
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
    var isActivityRestartNeeded = false

    when (index) {
        LOCALE_INDEX_DEFAULT -> {
            val defaultLocale = LocaleListCompat.getAdjustedDefault()[0]
            val newLayoutDirection = defaultLocale?.layoutDirection ?: -1
            isActivityRestartNeeded = isActivityRestartNeeded(newLayoutDirection)
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
        }
        LOCALE_INDEX_ENGLISH_UNITED_STATES -> {
            isActivityRestartNeeded = isActivityRestartNeeded(LayoutDirection.LTR)
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(LOCALE_ENGLISH_UNITED_STATES)
            )
        }
        LOCALE_INDEX_ENGLISH_GREAT_BRITAIN -> {
            isActivityRestartNeeded = isActivityRestartNeeded(LayoutDirection.LTR)
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(LOCALE_ENGLISH_GREAT_BRITAIN)
            )
        }
        LOCALE_INDEX_PERSIAN_IRAN -> {
            isActivityRestartNeeded = isActivityRestartNeeded(LayoutDirection.RTL)
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(LOCALE_PERSIAN_IRAN)
            )
        }
    }

    _changeCurrentLocaleLiveData.postValue(Event(isActivityRestartNeeded))
    Timber.i("isActivityRestartNeeded = $isActivityRestartNeeded}")
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

fun changeIsNotificationSoundActive(isActive: Boolean) = viewModelScope.launch {
    safeChangeIsNotificationSoundActive(isActive)
}

private suspend fun safeChangeIsNotificationSoundActive(isActive: Boolean) {
    _changeIsNotificationSoundActiveLiveData.postValue(Event(Resource.Loading()))
    try {
        preferencesRepository.isNotificationSoundActive(isActive)
        _changeIsNotificationSoundActiveLiveData.postValue(Event(Resource.Success(isActive)))
        Timber.i("Notification sound activation changed to $isActive")
    } catch (e: Exception) {
        _changeIsNotificationSoundActiveLiveData.postValue(
            Event(Resource.Error(UiText.DynamicString(e.message.toString())))
        )
        Timber.e("safeChangeIsNotificationSoundActive exception: ${e.message}")
    }
}

fun changeIsNotificationVibrateActive(isActive: Boolean) = viewModelScope.launch {
    safeChangeIsNotificationVibrateActive(isActive)
}

private suspend fun safeChangeIsNotificationVibrateActive(isActive: Boolean) {
    _changeIsNotificationVibrateActiveLiveData.postValue(Event(Resource.Loading()))
    try {
        preferencesRepository.isNotificationVibrateActive(isActive)
        _changeIsNotificationVibrateActiveLiveData.postValue(Event(Resource.Success(isActive)))
        Timber.i("Notification vibrate activation changed to $isActive")
    } catch (e: Exception) {
        _changeIsNotificationVibrateActiveLiveData.postValue(
            Event(Resource.Error(UiText.DynamicString(e.message.toString())))
        )
        Timber.e("safeChangeIsNotificationVibrateActive exception: ${e.message}")
    }
}

private fun isActivityRestartNeeded(newLayoutDirection: Int): Boolean {
    val currentLocale = AppCompatDelegate.getApplicationLocales()[0]
    val currentLayoutDirection = currentLocale?.layoutDirection

    return if (Build.VERSION.SDK_INT >= 33) {
        false
    } else currentLayoutDirection != newLayoutDirection
}
}
 */
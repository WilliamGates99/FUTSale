package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import androidx.appcompat.app.AppCompatDelegate
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.os.LocaleListCompat
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository.FakePreferencesRepositoryImp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.getOrAwaitValue
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.UiText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePreferencesRepository: FakePreferencesRepositoryImp
    private lateinit var testViewModel: SettingsViewModel

    @Before
    fun setUp() {
        fakePreferencesRepository = FakePreferencesRepositoryImp()
        testViewModel = SettingsViewModel(fakePreferencesRepository)
    }

    @Test
    fun getCurrentLanguage_returnsDefaultLanguageStringAndIndex() {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())

        val defaultLanguage = R.string.settings_text_settings_language_default
        val defaultLocaleIndex = 0

        testViewModel.getCurrentLanguage()

        val currentLanguageString = testViewModel.currentLanguageLiveData.getOrAwaitValue()
            .getContentIfNotHandled() as UiText.StringResource
        val indexResponseEvent = testViewModel.currentLocaleIndexLiveData.getOrAwaitValue()

        assertThat(currentLanguageString.resId).isEqualTo(defaultLanguage)
        assertThat(indexResponseEvent.getContentIfNotHandled()).isEqualTo(defaultLocaleIndex)
    }

    @Test
    fun getCurrentTheme_returnsDefaultThemeStringAndIndex() {
        val defaultThemeString = R.string.settings_text_settings_theme_default
        val defaultThemeIndex = 0

        testViewModel.getCurrentTheme()

        val currentThemeString = testViewModel.currentThemeLiveData.getOrAwaitValue()
            .getContentIfNotHandled() as UiText.StringResource
        val indexResponseEvent = testViewModel.currentThemeIndexLiveData.getOrAwaitValue()

        assertThat(currentThemeString.resId).isEqualTo(defaultThemeString)
        assertThat(indexResponseEvent.getContentIfNotHandled()).isEqualTo(defaultThemeIndex)
    }

    @Test
    fun getIsNotificationSoundActive_returnsDefaultIsNotificationSoundActive() {
        val defaultIsNotificationSoundActive = true

        testViewModel.getIsNotificationSoundActive()

        val responseEvent = testViewModel.isNotificationSoundActiveLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled())
            .isEqualTo(defaultIsNotificationSoundActive)
    }

    @Test
    fun getIsNotificationVibrateActive_returnsDefaultIsNotificationVibrateActive() {
        val defaultIsNotificationVibrateActive = true

        testViewModel.getIsNotificationVibrateActive()

        val responseEvent = testViewModel.isNotificationVibrateActiveLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled())
            .isEqualTo(defaultIsNotificationVibrateActive)
    }

    @Test
    fun changeCurrentLocale_returnsNewLocaleIndex() {
        val newLocaleIndex = 1

        testViewModel.changeCurrentLocale(newLocaleIndex)

        val responseEvent = testViewModel.changeCurrentLocaleLiveData.getOrAwaitValue()

        assertThat(responseEvent.getContentIfNotHandled()).isEqualTo(newLocaleIndex)
    }

    @Test
    fun changeCurrentTheme_returnsNewThemeStringAndIndex() {
        val newThemeString = R.string.settings_text_settings_theme_light
        val newThemeIndex = 1

        testViewModel.changeCurrentTheme(newThemeIndex)

        val currentThemeString = testViewModel.currentThemeLiveData.getOrAwaitValue()
            .getContentIfNotHandled() as UiText.StringResource
        val indexResponseEvent = testViewModel.currentThemeIndexLiveData.getOrAwaitValue()

        assertThat(currentThemeString.resId).isEqualTo(newThemeString)
        assertThat(indexResponseEvent.getContentIfNotHandled()).isEqualTo(newThemeIndex)
    }
}
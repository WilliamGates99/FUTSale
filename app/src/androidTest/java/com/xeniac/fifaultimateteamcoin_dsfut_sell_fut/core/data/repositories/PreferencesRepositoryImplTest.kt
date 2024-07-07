package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toAppThemeDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toPlatformDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mapper.toRateAppOptionDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.createTestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("DEPRECATION")
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class PreferencesRepositoryImplTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val testScope: TestCoroutineScope = createTestCoroutineScope(testDispatcher)

    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = { context.preferencesDataStoreFile(name = "settings_test") }
    )

    private val testRepository: PreferencesRepository = PreferencesRepositoryImpl(
        settingsDataStore = testDataStore
    )

    @Before
    fun setUp() {
        testScope.launch(
            context = testDispatcher
        ) {
            testDataStore.edit { it.clear() }
        }
    }

    @After
    fun tearDown() {
        testScope.cancel()
    }

    /*
    Fetch Initial Preferences Test Cases:
    getCurrentAppThemeSynchronously -> AppTheme.Default
    getCurrentAppTheme -> AppTheme.Default
    getCurrentAppLocale -> AppLocale.Default
    isOnBoardingCompleted -> false
    getNotificationPermissionCount -> 0
    isNotificationSoundEnabled -> true
    isNotificationVibrateEnabled -> true
    getSelectedRateAppOption -> RateAppOption.NOT_SHOWN_YET
    getPreviousRateAppRequestTimeInMs -> null
    getPartnerId -> null
    getSecretKey -> null
    getSelectedPlatform -> Platform.CONSOLE
     */
    @Test
    fun fetchInitialPreferences() = testScope.runBlockingTest {
        val initialAppThemeSynchronously = testRepository.getCurrentAppThemeSynchronously()
        val initialAppTheme = testRepository.getCurrentAppTheme()
        val initialAppLocale = testRepository.getCurrentAppLocale()
        val initialIsOnBoardingCompleted = testRepository.isOnBoardingCompleted()
        val initialNotificationPermissionCount = testRepository.getNotificationPermissionCount()
        val initialIsNotificationSoundEnabled = testRepository.isNotificationSoundEnabled()
        val initialIsNotificationVibrateEnabled = testRepository.isNotificationVibrateEnabled()
        val initialSelectedRateAppOption = testRepository.getSelectedRateAppOption()
        val initialPreviousRateAppRequestTime =
            testRepository.getPreviousRateAppRequestTimeInMs()
        val initialPartnerId = testRepository.getPartnerId()
        val initialSecretKey = testRepository.getSecretKey()
        val initialSelectedPlatform = testRepository.getSelectedPlatform()

        assertThat(initialAppThemeSynchronously).isEqualTo(AppTheme.Default)
        assertThat(initialAppTheme).isEqualTo(AppTheme.Default)
        assertThat(initialAppLocale).isEqualTo(AppLocale.Default)
        assertThat(initialIsOnBoardingCompleted).isFalse()
        assertThat(initialNotificationPermissionCount).isEqualTo(0)
        assertThat(initialIsNotificationSoundEnabled).isTrue()
        assertThat(initialIsNotificationVibrateEnabled).isTrue()
        assertThat(initialSelectedRateAppOption).isEqualTo(RateAppOption.NOT_SHOWN_YET)
        assertThat(initialPreviousRateAppRequestTime).isNull()
        assertThat(initialPartnerId).isNull()
        assertThat(initialSecretKey).isNull()
        assertThat(initialSelectedPlatform).isEqualTo(Platform.CONSOLE)
    }

    @Test
    fun writeCurrentAppTheme() = testScope.runBlockingTest {
        val testValue = AppTheme.Dark
        testRepository.setCurrentAppTheme(testValue.toAppThemeDto())

        val currentAppTheme = testRepository.getCurrentAppTheme()
        assertThat(currentAppTheme).isEqualTo(testValue)
    }

    @Test
    fun writeIsOnBoardingCompleted() = testScope.runBlockingTest {
        testRepository.isOnBoardingCompleted(true)

        val isOnBoardingCompleted = testRepository.isOnBoardingCompleted()
        assertThat(isOnBoardingCompleted).isTrue()
    }

    @Test
    fun writeNotificationPermissionCount() = testScope.runBlockingTest {
        val testValue = 2
        testRepository.setNotificationPermissionCount(testValue)

        val notificationPermissionCount = testRepository.getNotificationPermissionCount()
        assertThat(notificationPermissionCount).isEqualTo(testValue)
    }

    @Test
    fun writeIsNotificationSoundEnabled() = testScope.runBlockingTest {
        testRepository.isNotificationSoundEnabled(false)

        val isNotificationSoundEnabled = testRepository.isNotificationSoundEnabled()
        assertThat(isNotificationSoundEnabled).isFalse()
    }

    @Test
    fun writeIsNotificationVibrateEnabled() = testScope.runBlockingTest {
        testRepository.isNotificationVibrateEnabled(false)

        val isNotificationVibrateEnabled = testRepository.isNotificationVibrateEnabled()
        assertThat(isNotificationVibrateEnabled).isFalse()
    }

    @Test
    fun writeSelectedRateAppOption() = testScope.runBlockingTest {
        val testValue = RateAppOption.RATE_NOW
        testRepository.setSelectedRateAppOption(testValue.toRateAppOptionDto())

        val selectedRateAppOption = testRepository.getSelectedRateAppOption()
        assertThat(selectedRateAppOption).isEqualTo(testValue)
    }

    @Test
    fun writePreviousRateAppRequestTimeInMs() = testScope.runBlockingTest {
        testRepository.setPreviousRateAppRequestTimeInMs()

        val previousRateAppRequestTime = testRepository.getPreviousRateAppRequestTimeInMs()
        assertThat(previousRateAppRequestTime).isNotNull()
    }

    @Test
    fun writePartnerId() = testScope.runBlockingTest {
        val testValue = "123"
        testRepository.setPartnerId(testValue)

        val partnerId = testRepository.getPartnerId()
        assertThat(partnerId).isEqualTo(testValue)
    }

    @Test
    fun writeSecretKey() = testScope.runBlockingTest {
        val testValue = "abc123"
        testRepository.setSecretKey(testValue)

        val secretKey = testRepository.getSecretKey()
        assertThat(secretKey).isEqualTo(testValue)
    }

    @Test
    fun writeSelectedPlatform() = testScope.runBlockingTest {
        val testValue = Platform.PC
        testRepository.setSelectedPlatform(testValue.toPlatformDto())

        val selectedPlatform = testRepository.getSelectedPlatform()
        assertThat(selectedPlatform).isEqualTo(testValue)
    }
}
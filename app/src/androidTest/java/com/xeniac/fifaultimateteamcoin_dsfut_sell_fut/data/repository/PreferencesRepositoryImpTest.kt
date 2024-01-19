package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository

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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepositoryImp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Constants.DATASTORE_NAME_SETTINGS_TEST
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Constants.SELECTED_PLATFORM_CONSOLE
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Constants.SELECTED_PLATFORM_PC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class PreferencesRepositoryImpTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val testScope: TestScope = TestScope(testDispatcher + Job())

    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = { context.preferencesDataStoreFile(DATASTORE_NAME_SETTINGS_TEST) }
    )

    private val testRepository: PreferencesRepository = PreferencesRepositoryImp(testDataStore)

    @After
    fun tearDown() {
        testScope.cancel()
    }

    /**
     * Fetch Initial Preferences Test Cases:
     * getCurrentAppThemeSynchronously -> 0
     * isOnBoardingCompletedSynchronously -> false
     * isNotificationSoundActiveSynchronously -> true
     * isNotificationVibrateActive -> true
     * getCurrentAppTheme -> 0
     * isNotificationSoundActive -> true
     * isNotificationVibrateActive -> true
     * getRateAppDialogChoice -> 0
     * getPreviousRequestTimeInMillis -> 0L
     * getPartnerId -> null
     * getSecretKey -> null
     * getSelectedPlatform -> SELECTED_PLATFORM_CONSOLE
     */

    @Test
    fun fetchInitialSynchronousPreferences() = testScope.runTest {
        testDataStore.edit { it.clear() }

        val initialCurrentAppTheme = testRepository.getCurrentAppThemeIndexSynchronously()
        val initialIsOnBoardingCompleted = testRepository.isOnBoardingCompleted()
        val initialIsNotificationSoundActive =
            testRepository.isNotificationSoundActiveSynchronously()
        val initialIsNotificationVibrateActive =
            testRepository.isNotificationVibrateActiveSynchronously()

        assertThat(initialIsOnBoardingCompleted).isFalse()
        assertThat(initialCurrentAppTheme).isEqualTo(0)
        assertThat(initialIsNotificationSoundActive).isTrue()
        assertThat(initialIsNotificationVibrateActive).isTrue()
    }

    @Test
    fun fetchInitialPreferences() = testScope.runTest {
        testDataStore.edit { it.clear() }

        val initialCurrentAppTheme = testRepository.getCurrentAppThemeIndex()
        val initialIsNotificationSoundActive = testRepository.isNotificationSoundActive()
        val initialIsNotificationVibrateActive = testRepository.isNotificationVibrateActive()
        val initialRateAppDialogChoice = testRepository.getRateAppDialogChoice()
        val initialPreviousRequestTimeInMillis = testRepository.getPreviousRequestTimeInMillis()
        val initialPartnerId = testRepository.getPartnerId()
        val initialSecretKey = testRepository.getSecretKey()
        val initialSelectedPlatform = testRepository.getSelectedPlatform()

        assertThat(initialCurrentAppTheme).isEqualTo(0)
        assertThat(initialIsNotificationSoundActive).isTrue()
        assertThat(initialIsNotificationVibrateActive).isTrue()
        assertThat(initialRateAppDialogChoice).isEqualTo(0)
        assertThat(initialPreviousRequestTimeInMillis).isEqualTo(0L)
        assertThat(initialPartnerId).isNull()
        assertThat(initialSecretKey).isNull()
        assertThat(initialSelectedPlatform).isEqualTo(SELECTED_PLATFORM_CONSOLE)
    }

    /**
     * Write Preferences Test Cases:
     * isOnBoardingCompleted
     * setCurrentAppTheme
     * isNotificationSoundActive
     * isNotificationVibrateActive
     * setRateAppDialogChoice
     * setPreviousRequestTimeInMillis
     * setPartnerId
     * setSecretKey
     * setSelectedPlatform
     */

    @Test
    fun writeIsOnBoardingCompleted() = testScope.runTest {
        testDataStore.edit { it.clear() }

        testRepository.isOnBoardingCompleted(true)

        val isOnBoardingCompleted = testRepository.isOnBoardingCompleted()
        assertThat(isOnBoardingCompleted).isTrue()
    }

    @Test
    fun writeCurrentAppTheme() = testScope.runTest {
        testDataStore.edit { it.clear() }

        val testValue = 1
        testRepository.setCurrentAppTheme(testValue)

        val currentAppTheme = testRepository.getCurrentAppThemeIndex()
        assertThat(currentAppTheme).isEqualTo(testValue)
    }

    @Test
    fun writeIsNotificationSoundActive() = testScope.runTest {
        testDataStore.edit { it.clear() }

        testRepository.isNotificationSoundActive(false)

        val isNotificationSoundActive = testRepository.isNotificationSoundActive()
        assertThat(isNotificationSoundActive).isFalse()
    }

    @Test
    fun writeIsNotificationVibrateActive() = testScope.runTest {
        testDataStore.edit { it.clear() }

        testRepository.isNotificationVibrateActive(false)

        val isNotificationVibrateActive = testRepository.isNotificationVibrateActive()
        assertThat(isNotificationVibrateActive).isFalse()
    }

    @Test
    fun writeRateAppDialogChoice() = testScope.runTest {
        testDataStore.edit { it.clear() }

        val testValue = 1
        testRepository.setRateAppDialogChoice(testValue)

        val rateAppDialogChoice = testRepository.getRateAppDialogChoice()
        assertThat(rateAppDialogChoice).isEqualTo(testValue)
    }

    @Test
    fun writePreviousRequestTimeInMillis() = testScope.runTest {
        testDataStore.edit { it.clear() }

        val testValue = 100L
        testRepository.setPreviousRequestTimeInMillis(testValue)

        val previousRequestTimeInMillis = testRepository.getPreviousRequestTimeInMillis()
        assertThat(previousRequestTimeInMillis).isEqualTo(testValue)
    }

    @Test
    fun writePartnerId() = testScope.runTest {
        testDataStore.edit { it.clear() }

        val testValue = "123"
        testRepository.setPartnerId(testValue)

        val partnerId = testRepository.getPartnerId()
        assertThat(partnerId).isEqualTo(testValue)
    }

    @Test
    fun writeSecretKey() = testScope.runTest {
        testDataStore.edit { it.clear() }

        val testValue = "abc"
        testRepository.setSecretKey(testValue)

        val secretKey = testRepository.getSecretKey()
        assertThat(secretKey).isEqualTo(testValue)
    }

    @Test
    fun writeSelectedPlatform() = testScope.runTest {
        testDataStore.edit { it.clear() }

        val testValue = SELECTED_PLATFORM_PC
        testRepository.setSelectedPlatform(testValue)

        val selectedPlatform = testRepository.getSelectedPlatform()
        assertThat(selectedPlatform).isEqualTo(testValue)
    }
}
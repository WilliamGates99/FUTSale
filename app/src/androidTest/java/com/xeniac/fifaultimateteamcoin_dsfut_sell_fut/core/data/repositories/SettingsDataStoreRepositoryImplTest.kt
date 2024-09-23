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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class SettingsDataStoreRepositoryImplTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val testScope: TestScope = TestScope(context = testDispatcher)

    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope.backgroundScope,
        produceFile = { context.preferencesDataStoreFile(name = "Settings-Test") }
    )

    private val testRepository: SettingsDataStoreRepository = SettingsDataStoreRepositoryImpl(
        dataStore = testDataStore
    )

    @Before
    fun setUp() {
        testScope.launch {
            testDataStore.edit { it.clear() }
        }
    }

    @After
    fun tearDown() {
        testScope.cancel()
    }

    /*
    Fetch Initial Preferences Test Cases:
    isOnboardingCompleted -> false
    getNotificationPermissionCount -> 0
    getCurrentAppThemeSynchronously -> AppTheme.Default
    getCurrentAppTheme -> AppTheme.Default
    getCurrentAppLocale -> AppLocale.Default
    isNotificationSoundEnabled -> true
    isNotificationVibrateEnabled -> true
     */
    @Test
    fun fetchInitialPreferences() = testScope.runTest {
        val initialIsOnboardingCompleted = testRepository.isOnboardingCompleted()
        val initialNotificationPermissionCount = testRepository
            .getNotificationPermissionCount().first()
        val initialAppThemeSynchronously = testRepository.getCurrentAppThemeSynchronously()
        val initialAppTheme = testRepository.getCurrentAppTheme().first()
        val initialAppLocale = testRepository.getCurrentAppLocale()
        val initialIsNotificationSoundEnabled = testRepository.isNotificationSoundEnabled().first()
        val initialIsNotificationVibrateEnabled = testRepository
            .isNotificationVibrateEnabled().first()

        assertThat(initialIsOnboardingCompleted).isFalse()
        assertThat(initialNotificationPermissionCount).isEqualTo(0)
        assertThat(initialAppThemeSynchronously).isEqualTo(AppTheme.Default)
        assertThat(initialAppTheme).isEqualTo(AppTheme.Default)
        assertThat(initialAppLocale).isEqualTo(AppLocale.Default)
        assertThat(initialIsNotificationSoundEnabled).isTrue()
        assertThat(initialIsNotificationVibrateEnabled).isTrue()
    }

    @Test
    fun writeIsOnboardingCompleted() = testScope.runTest {
        testRepository.isOnBoardingCompleted(true)

        val isOnboardingCompleted = testRepository.isOnboardingCompleted()
        assertThat(isOnboardingCompleted).isTrue()
    }

    @Test
    fun writeNotificationPermissionCount() = testScope.runTest {
        val testValue = 2
        testRepository.storeNotificationPermissionCount(testValue)

        val notificationPermissionCount = testRepository.getNotificationPermissionCount().first()
        assertThat(notificationPermissionCount).isEqualTo(testValue)
    }

    @Test
    fun writeCurrentAppTheme() = testScope.runTest {
        val testValue = AppTheme.Dark
        testRepository.storeCurrentAppTheme(testValue)

        val currentAppTheme = testRepository.getCurrentAppTheme().first()
        assertThat(currentAppTheme).isEqualTo(testValue)
    }

    @Test
    fun writeIsNotificationSoundEnabled() = testScope.runTest {
        testRepository.isNotificationSoundEnabled(false)

        val isNotificationSoundEnabled = testRepository.isNotificationSoundEnabled().first()
        assertThat(isNotificationSoundEnabled).isFalse()
    }

    @Test
    fun writeIsNotificationVibrateEnabled() = testScope.runTest {
        testRepository.isNotificationVibrateEnabled(false)

        val isNotificationVibrateEnabled = testRepository.isNotificationVibrateEnabled().first()
        assertThat(isNotificationVibrateEnabled).isFalse()
    }
}
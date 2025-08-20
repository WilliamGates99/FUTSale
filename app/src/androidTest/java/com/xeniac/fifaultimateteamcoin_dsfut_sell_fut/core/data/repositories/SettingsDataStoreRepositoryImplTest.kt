package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.SettingsPreferences
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.SettingsPreferencesSerializer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
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

    private val testDataStore: DataStore<SettingsPreferences> = DataStoreFactory.create(
        serializer = SettingsPreferencesSerializer,
        corruptionHandler = ReplaceFileCorruptionHandler { SettingsPreferences() },
        scope = testScope.backgroundScope,
        produceFile = { context.preferencesDataStoreFile(name = "Settings-Test.pb") }
    )

    private val testRepository: SettingsDataStoreRepository = SettingsDataStoreRepositoryImpl(
        dataStore = testDataStore
    )

    @Before
    fun setUp() {
        runBlocking {
            testDataStore.updateData { SettingsPreferences() }
        }
    }

    @After
    fun tearDown() {
        testScope.cancel()
    }

    /*
    Fetch Initial Preferences Test Cases:
    getCurrentAppThemeSynchronously -> AppTheme.DEFAULT
    getCurrentAppTheme -> AppTheme.DEFAULT
    getCurrentAppLocale -> AppLocale.DEFAULT
    isNotificationSoundEnabled -> true
    isNotificationVibrateEnabled -> true
     */
    @Test
    fun fetchInitialPreferences() = testScope.runTest {
        with(testRepository) {
            val initialAppThemeSynchronously = getCurrentAppThemeSynchronously()
            val initialAppTheme = getCurrentAppTheme().first()
            val initialAppLocale = getCurrentAppLocale()
            val initialIsNotificationSoundEnabled = isNotificationSoundEnabled().first()
            val initialIsNotificationVibrateEnabled = isNotificationVibrateEnabled().first()

            assertThat(initialAppThemeSynchronously).isEqualTo(AppTheme.DEFAULT)
            assertThat(initialAppTheme).isEqualTo(AppTheme.DEFAULT)
            assertThat(initialAppLocale).isEqualTo(AppLocale.DEFAULT)
            assertThat(initialIsNotificationSoundEnabled).isTrue()
            assertThat(initialIsNotificationVibrateEnabled).isTrue()
        }
    }

    @Test
    fun writeCurrentAppTheme() = testScope.runTest {
        val testValue = AppTheme.DARK
        testRepository.storeCurrentAppTheme(appTheme = testValue)

        testRepository.getCurrentAppTheme().onEach { currentAppTheme ->
            assertThat(currentAppTheme).isEqualTo(testValue)
        }
    }

    @Test
    fun writeIsNotificationSoundEnabled() = testScope.runTest {
        testRepository.isNotificationSoundEnabled(isEnabled = false)

        testRepository.isNotificationSoundEnabled().onEach { isNotificationSoundEnabled ->
            assertThat(isNotificationSoundEnabled).isFalse()
        }
    }

    @Test
    fun writeIsNotificationVibrateEnabled() = testScope.runTest {
        testRepository.isNotificationVibrateEnabled(isEnabled = false)

        testRepository.isNotificationVibrateEnabled().onEach { isNotificationVibrateEnabled ->
            assertThat(isNotificationVibrateEnabled).isFalse()
        }
    }
}
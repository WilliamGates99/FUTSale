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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.DsfutPreferences
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.DsfutPreferencesSerializer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
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
class DsfutDataStoreRepositoryImplTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val testScope: TestScope = TestScope(context = testDispatcher)

    private val testDataStore: DataStore<DsfutPreferences> = DataStoreFactory.create(
        serializer = DsfutPreferencesSerializer,
        corruptionHandler = ReplaceFileCorruptionHandler { DsfutPreferences() },
        scope = testScope.backgroundScope,
        produceFile = { context.preferencesDataStoreFile(name = "Dsfut-Test.pb") }
    )

    private val testRepository: DsfutDataStoreRepository = DsfutDataStoreRepositoryImpl(
        dataStore = testDataStore
    )

    @Before
    fun setUp() {
        runBlocking {
            testDataStore.updateData { DsfutPreferences() }
        }
    }

    @After
    fun tearDown() {
        testScope.cancel()
    }

    /*
    Fetch Initial Preferences Test Cases:
    isOnboardingCompleted -> false
    getPartnerId -> null
    getSecretKey -> null
    getSelectedPlatform -> Platform.CONSOLE
     */
    @Test
    fun fetchInitialPreferences() = testScope.runTest {
        with(testRepository) {
            val initialIsOnboardingCompleted = isOnboardingCompleted()
            val initialPartnerId = getPartnerId()
            val initialSecretKey = getSecretKey()
            val initialSelectedPlatform = getSelectedPlatform().first()

            assertThat(initialIsOnboardingCompleted).isFalse()
            assertThat(initialPartnerId).isNull()
            assertThat(initialSecretKey).isNull()
            assertThat(initialSelectedPlatform).isEqualTo(Platform.CONSOLE)
        }
    }

    @Test
    fun writeIsOnboardingCompleted() = testScope.runTest {
        testRepository.isOnboardingCompleted(isCompleted = true)

        val isOnboardingCompleted = testRepository.isOnboardingCompleted()
        assertThat(isOnboardingCompleted).isTrue()
    }

    @Test
    fun writePartnerId() = testScope.runTest {
        val testValue = "123"
        testRepository.storePartnerId(partnerId = testValue)

        val partnerId = testRepository.getPartnerId()
        assertThat(partnerId).isEqualTo(testValue)
    }

    @Test
    fun writeSecretKey() = testScope.runTest {
        val testValue = "abc123"
        testRepository.storeSecretKey(secretKey = testValue)

        val secretKey = testRepository.getSecretKey()
        assertThat(secretKey).isEqualTo(testValue)
    }

    @Test
    fun writeSelectedPlatform() = testScope.runTest {
        val testValue = Platform.PC
        testRepository.storeSelectedPlatform(platform = testValue)

        testRepository.getSelectedPlatform().onEach { selectedPlatform ->
            assertThat(selectedPlatform).isEqualTo(testValue)
        }
    }
}
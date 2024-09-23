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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
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
class DsfutDataStoreRepositoryImplTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val testScope: TestScope = TestScope(context = testDispatcher)

    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope.backgroundScope,
        produceFile = { context.preferencesDataStoreFile(name = "Dsfut-Test") }
    )

    private val testRepository: DsfutDataStoreRepository = DsfutDataStoreRepositoryImpl(
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
    getPartnerId -> null
    getSecretKey -> null
    getSelectedPlatform -> Platform.CONSOLE
     */
    @Test
    fun fetchInitialPreferences() = testScope.runTest {
        val initialPartnerId = testRepository.getPartnerId().first()
        val initialSecretKey = testRepository.getSecretKey().first()
        val initialSelectedPlatform = testRepository.getSelectedPlatform().first()

        assertThat(initialPartnerId).isNull()
        assertThat(initialSecretKey).isNull()
        assertThat(initialSelectedPlatform).isEqualTo(Platform.CONSOLE)
    }

    @Test
    fun writePartnerId() = testScope.runTest {
        val testValue = "123"
        testRepository.storePartnerId(testValue)

        val partnerId = testRepository.getPartnerId().first()
        assertThat(partnerId).isEqualTo(testValue)
    }

    @Test
    fun writeSecretKey() = testScope.runTest {
        val testValue = "abc123"
        testRepository.storeSecretKey(testValue)

        val secretKey = testRepository.getSecretKey().first()
        assertThat(secretKey).isEqualTo(testValue)
    }

    @Test
    fun writeSelectedPlatform() = testScope.runTest {
        val testValue = Platform.PC
        testRepository.storeSelectedPlatform(testValue)

        val selectedPlatform = testRepository.getSelectedPlatform().first()
        assertThat(selectedPlatform).isEqualTo(testValue)
    }
}
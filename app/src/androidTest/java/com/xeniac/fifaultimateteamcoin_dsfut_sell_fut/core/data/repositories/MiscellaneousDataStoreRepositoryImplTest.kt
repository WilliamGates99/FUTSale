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
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.MiscellaneousPreferences
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.MiscellaneousPreferencesSerializer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.MiscellaneousDataStoreRepository
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
class MiscellaneousDataStoreRepositoryImplTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val testScope: TestScope = TestScope(context = testDispatcher)

    private val testDataStore: DataStore<MiscellaneousPreferences> = DataStoreFactory.create(
        serializer = MiscellaneousPreferencesSerializer,
        corruptionHandler = ReplaceFileCorruptionHandler { MiscellaneousPreferences() },
        scope = testScope.backgroundScope,
        produceFile = { context.preferencesDataStoreFile(name = "Miscellaneous-Test.pb") }
    )

    private val testRepository: MiscellaneousDataStoreRepository =
        MiscellaneousDataStoreRepositoryImpl(dataStore = testDataStore)

    @Before
    fun setUp() {
        runBlocking {
            testDataStore.updateData { MiscellaneousPreferences() }
        }
    }

    @After
    fun tearDown() {
        testScope.cancel()
    }

    /*
    Fetch Initial Preferences Test Cases:
    getAppUpdateDialogShowCount -> 0
    isAppUpdateDialogShownToday -> false
    getSelectedRateAppOption -> RateAppOption.NOT_SHOWN_YET
    getPreviousRateAppRequestDateTime -> null
     */
    @Test
    fun fetchInitialPreferences() = testScope.runTest {
        with(testRepository) {
            val initialAppUpdateDialogShowCount = getAppUpdateDialogShowCount().first()
            val initialIsAppUpdateDialogShownToday = isAppUpdateDialogShownToday().first()
            val initialSelectedRateAppOption = getSelectedRateAppOption().first()
            val initialPreviousRateAppRequestDateTime = getPreviousRateAppRequestDateTime().first()

            assertThat(initialAppUpdateDialogShowCount).isEqualTo(0)
            assertThat(initialIsAppUpdateDialogShownToday).isFalse()
            assertThat(initialSelectedRateAppOption).isEqualTo(RateAppOption.NOT_SHOWN_YET)
            assertThat(initialPreviousRateAppRequestDateTime).isNull()
        }
    }

    @Test
    fun writeAppUpdateDialogShowCount() = testScope.runTest {
        val testValue = 3
        testRepository.storeAppUpdateDialogShowCount(count = testValue)

        testRepository.getAppUpdateDialogShowCount().onEach { appUpdateDialogShowCount ->
            assertThat(appUpdateDialogShowCount).isEqualTo(testValue)
        }
    }

    @Test
    fun writeAppUpdateDialogShowDateTime() = testScope.runTest {
        testRepository.storeAppUpdateDialogShowDateTime()

        testRepository.isAppUpdateDialogShownToday().onEach { isAppUpdateDialogShownTodayBefore ->
            assertThat(isAppUpdateDialogShownTodayBefore).isTrue()
        }
    }

    @Test
    fun removeAppUpdateDialogShowDateTime() = testScope.runTest {
        testRepository.storeAppUpdateDialogShowDateTime()

        testRepository.isAppUpdateDialogShownToday().onEach { isAppUpdateDialogShownTodayBefore ->
            assertThat(isAppUpdateDialogShownTodayBefore).isTrue()
        }

        testRepository.removeAppUpdateDialogShowDateTime()

        testRepository.isAppUpdateDialogShownToday().onEach { isAppUpdateDialogShownTodayAfter ->
            assertThat(isAppUpdateDialogShownTodayAfter).isFalse()
        }
    }

    @Test
    fun writeSelectedRateAppOption() = testScope.runTest {
        val testValue = RateAppOption.RATE_NOW
        testRepository.storeSelectedRateAppOption(rateAppOption = testValue)

        testRepository.getSelectedRateAppOption().onEach { selectedRateAppOption ->
            assertThat(selectedRateAppOption).isEqualTo(testValue)
        }
    }

    @Test
    fun writePreviousRateAppRequestDateTime() = testScope.runTest {
        testRepository.storePreviousRateAppRequestDateTime()

        testRepository.getPreviousRateAppRequestDateTime().onEach { previousRateAppRequestTime ->
            assertThat(previousRateAppRequestTime).isNotNull()
        }
    }
}
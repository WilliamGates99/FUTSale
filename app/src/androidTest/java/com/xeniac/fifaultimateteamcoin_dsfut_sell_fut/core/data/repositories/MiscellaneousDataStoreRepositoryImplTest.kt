package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
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
        scope = testScope.backgroundScope,
        produceFile = { context.preferencesDataStoreFile(name = "Miscellaneous-Test.pb") }
    )

    private val testRepository: MiscellaneousDataStoreRepository =
        MiscellaneousDataStoreRepositoryImpl(dataStore = testDataStore)

    @Before
    fun setUp() {
        testScope.launch {
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
    getPreviousRateAppRequestTimeInMs -> null
     */
    @Test
    fun fetchInitialPreferences() = testScope.runTest {
        val initialAppUpdateDialogShowCount = testRepository.getAppUpdateDialogShowCount().first()
        val initialIsAppUpdateDialogShownToday = testRepository
            .isAppUpdateDialogShownToday().first()
        val initialSelectedRateAppOption = testRepository.getSelectedRateAppOption().first()
        val initialPreviousRateAppRequestTime = testRepository
            .getPreviousRateAppRequestTimeInMs().first()

        assertThat(initialAppUpdateDialogShowCount).isEqualTo(0)
        assertThat(initialIsAppUpdateDialogShownToday).isFalse()
        assertThat(initialSelectedRateAppOption).isEqualTo(RateAppOption.NOT_SHOWN_YET)
        assertThat(initialPreviousRateAppRequestTime).isNull()
    }

    @Test
    fun writeAppUpdateDialogShowCount() = testScope.runTest {
        val testValue = 3
        testRepository.storeAppUpdateDialogShowCount(testValue)

        val appUpdateDialogShowCount = testRepository.getAppUpdateDialogShowCount().first()
        assertThat(appUpdateDialogShowCount).isEqualTo(testValue)
    }

    @Test
    fun writeAppUpdateDialogShowEpochDays() = testScope.runTest {
        testRepository.storeAppUpdateDialogShowEpochDays()

        val isAppUpdateDialogShownTodayBefore = testRepository.isAppUpdateDialogShownToday().first()
        assertThat(isAppUpdateDialogShownTodayBefore).isTrue()
    }

    @Test
    fun removeAppUpdateDialogShowEpochDays() = testScope.runTest {
        testRepository.storeAppUpdateDialogShowEpochDays()

        val isAppUpdateDialogShownTodayBefore = testRepository.isAppUpdateDialogShownToday().first()
        assertThat(isAppUpdateDialogShownTodayBefore).isTrue()

        testRepository.removeAppUpdateDialogShowEpochDays()

        val isAppUpdateDialogShownTodayAfter = testRepository.isAppUpdateDialogShownToday().first()
        assertThat(isAppUpdateDialogShownTodayAfter).isFalse()
    }

    @Test
    fun writeSelectedRateAppOption() = testScope.runTest {
        val testValue = RateAppOption.RATE_NOW
        testRepository.storeSelectedRateAppOption(testValue)

        val selectedRateAppOption = testRepository.getSelectedRateAppOption().first()
        assertThat(selectedRateAppOption).isEqualTo(testValue)
    }

    @Test
    fun writePreviousRateAppRequestTimeInMs() = testScope.runTest {
        testRepository.storePreviousRateAppRequestTimeInMs()

        val previousRateAppRequestTime = testRepository.getPreviousRateAppRequestTimeInMs().first()
        assertThat(previousRateAppRequestTime).isNotNull()
    }
}
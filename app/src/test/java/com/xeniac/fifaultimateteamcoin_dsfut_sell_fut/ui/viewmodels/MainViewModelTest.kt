package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository.FakePreferencesRepositoryImp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePreferencesRepository: PreferencesRepository
    private lateinit var testViewModel: MainViewModel

    @Before
    fun setUp() {
        fakePreferencesRepository = FakePreferencesRepositoryImp()
        testViewModel = MainViewModel(fakePreferencesRepository)
    }

    @Test
    fun isOnBoardingCompleted_returnsDefaultIsOnBoardingCompletedValue() {
        val defaultIsOnBoardingCompleted = false

        val isOnBoardingCompleted = testViewModel.isOnBoardingCompleted()
        assertThat(isOnBoardingCompleted).isEqualTo(defaultIsOnBoardingCompleted)
    }

    @Test
    fun getRateAppDialogChoice_returnsDefaultRateAppDialogChoice() {
        val defaultRateAppDialogChoice = 0
        testViewModel.getRateAppDialogChoice()

        val responseEvent = testViewModel.rateAppDialogChoiceLiveData.getOrAwaitValue()
        assertThat(responseEvent.getContentIfNotHandled()).isEqualTo(defaultRateAppDialogChoice)
    }

    @Test
    fun getPreviousRequestTimeInMillis_returnsDefaultPreviousRequestTimeInMillis() {
        val defaultPreviousRequestTimeInMillis = 0L
        testViewModel.getPreviousRequestTimeInMillis()

        val responseEvent = testViewModel.previousRequestTimeInMillisLiveData.getOrAwaitValue()
        assertThat(responseEvent.getContentIfNotHandled())
            .isEqualTo(defaultPreviousRequestTimeInMillis)
    }

    @Test
    fun setRateAppDialogChoice_returnsNewRateAppDialogChoice() {
        val newRateAppDialogChoice = 1
        testViewModel.setRateAppDialogChoice(newRateAppDialogChoice)

        val responseEvent = testViewModel.rateAppDialogChoiceLiveData.getOrAwaitValue()
        assertThat(responseEvent.getContentIfNotHandled()).isEqualTo(newRateAppDialogChoice)
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository.FakePreferencesRepositoryImp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
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
    fun getIsOnBoardingCompleted_returnsDefaultIsOnBoardingCompleted() {
        val defaultIsOnBoardingCompleted = false

        testViewModel.getIsOnBoardingCompleted()
        val isOnBoardingCompleted = testViewModel.isOnBoardingCompletedLiveData.getOrAwaitValue()
        assertThat(isOnBoardingCompleted).isEqualTo(defaultIsOnBoardingCompleted)
    }
}
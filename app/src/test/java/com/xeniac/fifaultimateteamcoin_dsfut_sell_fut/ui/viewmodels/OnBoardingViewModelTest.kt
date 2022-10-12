package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.FakePreferencesRepositoryImp
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.getOrAwaitValue
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OnBoardingViewModelTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePreferencesRepository: PreferencesRepository
    private lateinit var testViewModel: OnBoardingViewModel

    @Before
    fun setUp() {
        fakePreferencesRepository = FakePreferencesRepositoryImp()
        testViewModel = OnBoardingViewModel(fakePreferencesRepository)
    }

    @Test
    fun getIsOnBoardingCompleted_returnsDefaultIsOnBoardingCompleted() {
        val defaultIsOnBoardingCompleted = false

        testViewModel.getIsOnBoardingCompleted()
        val isOnBoardingCompleted = testViewModel.isOnBoardingCompletedLiveData.getOrAwaitValue()
        assertThat(isOnBoardingCompleted).isEqualTo(defaultIsOnBoardingCompleted)
    }

    @Test
    fun validateFourthScreenInputsWithBlankInputs_returnsError() {
        testViewModel.validateFourthScreenInputs("", "")

        val responseEvent = testViewModel.completeOnBoardingLiveData.getOrAwaitValue()
        assertThat(responseEvent.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun validateFourthScreenInputsWithValidInputs_returnsSuccess() {
        testViewModel.validateFourthScreenInputs("123", "abc")

        val responseEvent = testViewModel.completeOnBoardingLiveData.getOrAwaitValue()
        assertThat(responseEvent.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun completeOnBoarding_setsIsOnBoardingCompletedToTrueAndStoresPartnerIdAndSecretKey() =
        runTest {
            val partnerId = "123"
            val secretKey = "abc"

            testViewModel.completeOnBoarding(partnerId, secretKey)

            val isOnBoardingCompleted = fakePreferencesRepository.isOnBoardingCompleted()
            val storedPartnerId = fakePreferencesRepository.getPartnerId()
            val storedSecretKey = fakePreferencesRepository.getSecretKey()

            assertThat(isOnBoardingCompleted).isTrue()
            assertThat(storedPartnerId).isEqualTo(partnerId)
            assertThat(storedSecretKey).isEqualTo(secretKey)
        }
}
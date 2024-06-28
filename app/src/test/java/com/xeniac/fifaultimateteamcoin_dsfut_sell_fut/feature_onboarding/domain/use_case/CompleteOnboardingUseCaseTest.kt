package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakePreferencesRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class CompleteOnboardingUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePreferencesRepository: PreferencesRepository
    private lateinit var completeOnboardingUseCase: CompleteOnboardingUseCase

    @Before
    fun setUp() {
        fakePreferencesRepository = FakePreferencesRepositoryImpl()
        completeOnboardingUseCase = CompleteOnboardingUseCase(fakePreferencesRepository)
    }

    @Test
    fun completeOnboarding_setsIsOnboardingCompletedToTrueAndStoresPartnerIdAndSecretKey() {
        runTest {
            val partnerId = "123"
            val secretKey = "abc123"

            completeOnboardingUseCase(
                partnerId = partnerId,
                secretKey = secretKey
            )

            val isOnBoardingCompleted = fakePreferencesRepository.isOnBoardingCompleted()
            val storedPartnerId = fakePreferencesRepository.getPartnerId()
            val storedSecretKey = fakePreferencesRepository.getSecretKey()

            advanceUntilIdle()

            assertThat(isOnBoardingCompleted).isTrue()
            assertThat(storedPartnerId).isEqualTo(partnerId)
            assertThat(storedSecretKey).isEqualTo(secretKey)
        }
    }
}
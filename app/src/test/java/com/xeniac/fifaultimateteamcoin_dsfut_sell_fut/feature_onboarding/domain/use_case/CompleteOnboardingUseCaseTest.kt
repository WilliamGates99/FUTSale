package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeDsfutDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeSettingsDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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

    private lateinit var fakeSettingsDataStoreRepositoryImpl: FakeSettingsDataStoreRepositoryImpl
    private lateinit var fakeDsfutDataStoreRepositoryImpl: FakeDsfutDataStoreRepositoryImpl
    private lateinit var completeOnboardingUseCase: CompleteOnboardingUseCase

    @Before
    fun setUp() {
        fakeSettingsDataStoreRepositoryImpl = FakeSettingsDataStoreRepositoryImpl()
        fakeDsfutDataStoreRepositoryImpl = FakeDsfutDataStoreRepositoryImpl()
        completeOnboardingUseCase = CompleteOnboardingUseCase(
            settingsDataStoreRepository = fakeSettingsDataStoreRepositoryImpl,
            dsfutDataStoreRepository = fakeDsfutDataStoreRepositoryImpl
        )
    }

    @Test
    fun completeOnboarding_returnsSuccess() = runTest {
        val partnerId = "123"
        val secretKey = "abc123"

        val result = completeOnboardingUseCase(
            partnerId = partnerId,
            secretKey = secretKey
        ).first()

        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun completeOnboarding_setsIsOnboardingCompletedToTrueAndStoresPartnerIdAndSecretKey() {
        runTest {
            val partnerId = "123"
            val secretKey = "abc123"

            val result = completeOnboardingUseCase(
                partnerId = partnerId,
                secretKey = secretKey
            ).first()

            val isOnBoardingCompleted = fakeSettingsDataStoreRepositoryImpl.isOnboardingCompleted()
            val storedPartnerId = fakeDsfutDataStoreRepositoryImpl.getPartnerId().first()
            val storedSecretKey = fakeDsfutDataStoreRepositoryImpl.getSecretKey().first()

            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat(isOnBoardingCompleted).isTrue()
            assertThat(storedPartnerId).isEqualTo(partnerId)
            assertThat(storedSecretKey).isEqualTo(secretKey)
        }
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeDsfutDataStoreRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class GetIsOnboardingCompletedUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeDsfutDataStoreRepositoryImpl: FakeDsfutDataStoreRepositoryImpl
    private lateinit var getIsOnboardingCompletedUseCase: GetIsOnboardingCompletedUseCase

    @Before
    fun setUp() {
        fakeDsfutDataStoreRepositoryImpl = FakeDsfutDataStoreRepositoryImpl()
        getIsOnboardingCompletedUseCase = GetIsOnboardingCompletedUseCase(
            dsfutDataStoreRepository = fakeDsfutDataStoreRepositoryImpl
        )
    }

    @Test
    fun getIsOnboardingCompleted_returnsCurrentIsOnboardingCompletedValue() = runTest {
        getIsOnboardingCompletedUseCase().onEach { currentIsOnboardingCompleted ->
            assertThat(currentIsOnboardingCompleted).isEqualTo(
                fakeDsfutDataStoreRepositoryImpl.isOnBoardingCompleted
            )
        }
    }
}
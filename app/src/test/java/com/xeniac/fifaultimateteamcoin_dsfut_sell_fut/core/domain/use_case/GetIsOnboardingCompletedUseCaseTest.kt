package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeSettingsDataStoreRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private lateinit var fakeSettingsDataStoreRepositoryImpl: FakeSettingsDataStoreRepositoryImpl
    private lateinit var getIsOnboardingCompletedUseCase: GetIsOnboardingCompletedUseCase

    @Before
    fun setUp() {
        fakeSettingsDataStoreRepositoryImpl = FakeSettingsDataStoreRepositoryImpl()
        getIsOnboardingCompletedUseCase = GetIsOnboardingCompletedUseCase(
            settingsDataStoreRepository = fakeSettingsDataStoreRepositoryImpl
        )
    }

    @Test
    fun getIsOnboardingCompleted_returnsCurrentIsOnboardingCompletedValue() = runTest {
        val currentIsOnboardingCompleted = getIsOnboardingCompletedUseCase()
        assertThat(currentIsOnboardingCompleted).isEqualTo(
            fakeSettingsDataStoreRepositoryImpl.isOnBoardingCompleted
        )
    }
}
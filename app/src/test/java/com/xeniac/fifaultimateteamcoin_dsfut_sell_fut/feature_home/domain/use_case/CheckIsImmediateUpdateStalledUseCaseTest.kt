package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories.FakeHomeRepositoryImpl
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
class CheckIsImmediateUpdateStalledUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeHomeRepository: FakeHomeRepositoryImpl
    private lateinit var checkIsImmediateUpdateStalledUseCase: CheckIsImmediateUpdateStalledUseCase

    @Before
    fun setUp() {
        fakeHomeRepository = FakeHomeRepositoryImpl()
        checkIsImmediateUpdateStalledUseCase = CheckIsImmediateUpdateStalledUseCase(
            homeRepository = fakeHomeRepository
        )
    }

    @Test
    fun checkIsImmediateUpdateStalledWithNoUpdateDownloaded_returnsNull() = runTest {
        fakeHomeRepository.isImmediateUpdateStalled(isStalled = false)
        val appUpdateInfo = checkIsImmediateUpdateStalledUseCase().first()
        assertThat(appUpdateInfo).isNull()
    }

    @Test
    fun checkIsFlexibleUpdateStalledWithUpdateDownloaded_returnsAppUpdateInfo() = runTest {
        fakeHomeRepository.isImmediateUpdateStalled(isStalled = true)
        val appUpdateInfo = checkIsImmediateUpdateStalledUseCase().first()
        assertThat(appUpdateInfo).isNotNull()
    }
}
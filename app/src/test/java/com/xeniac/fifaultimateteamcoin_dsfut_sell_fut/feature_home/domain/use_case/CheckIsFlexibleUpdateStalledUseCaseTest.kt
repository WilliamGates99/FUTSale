package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeMiscellaneousDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories.FakeAppUpdateRepositoryImpl
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
class CheckIsFlexibleUpdateStalledUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeMiscellaneousDataStoreRepositoryImpl: FakeMiscellaneousDataStoreRepositoryImpl
    private lateinit var fakeAppUpdateRepository: FakeAppUpdateRepositoryImpl
    private lateinit var checkIsFlexibleUpdateStalledUseCase: CheckIsFlexibleUpdateStalledUseCase

    @Before
    fun setUp() {
        fakeMiscellaneousDataStoreRepositoryImpl = FakeMiscellaneousDataStoreRepositoryImpl()
        fakeAppUpdateRepository = FakeAppUpdateRepositoryImpl(
            miscellaneousDataStoreRepository = fakeMiscellaneousDataStoreRepositoryImpl
        )

        checkIsFlexibleUpdateStalledUseCase = CheckIsFlexibleUpdateStalledUseCase(
            appUpdateRepository = fakeAppUpdateRepository
        )
    }

    @Test
    fun checkIsFlexibleUpdateStalledWithNoUpdateDownloaded_returnsFalse() = runTest {
        fakeAppUpdateRepository.isFlexibleUpdateStalled(isStalled = false)
        val isUpdateDownloaded = checkIsFlexibleUpdateStalledUseCase().first()
        assertThat(isUpdateDownloaded).isFalse()
    }

    @Test
    fun checkIsFlexibleUpdateStalledWithUpdateDownloaded_returnsTrue() = runTest {
        fakeAppUpdateRepository.isFlexibleUpdateStalled(isStalled = true)
        val isUpdateDownloaded = checkIsFlexibleUpdateStalledUseCase().first()
        assertThat(isUpdateDownloaded).isTrue()
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeMiscellaneousDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories.FakeHomeRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class CheckFlexibleUpdateDownloadStateUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeMiscellaneousDataStoreRepositoryImpl: FakeMiscellaneousDataStoreRepositoryImpl
    private lateinit var fakeHomeRepository: FakeHomeRepositoryImpl
    private lateinit var checkFlexibleUpdateDownloadStateUseCase: CheckFlexibleUpdateDownloadStateUseCase

    @Before
    fun setUp() {
        fakeMiscellaneousDataStoreRepositoryImpl = FakeMiscellaneousDataStoreRepositoryImpl()
        fakeHomeRepository = FakeHomeRepositoryImpl(
            miscellaneousDataStoreRepository = fakeMiscellaneousDataStoreRepositoryImpl
        )

        checkFlexibleUpdateDownloadStateUseCase = CheckFlexibleUpdateDownloadStateUseCase(
            homeRepository = fakeHomeRepository
        )
    }

    @Test
    fun checkFlexibleUpdateDownloadStateWithNoUpdateDownloaded_returnsNothing() = runTest {
        fakeHomeRepository.isFlexibleUpdateDownloaded(isDownloaded = false)
        val isUpdateDownloaded = checkFlexibleUpdateDownloadStateUseCase().firstOrNull()
        assertThat(isUpdateDownloaded).isNull()
    }

    @Test
    fun checkFlexibleUpdateDownloadStateWithUpdateDownloaded_returnsTrue() = runTest {
        fakeHomeRepository.isFlexibleUpdateDownloaded(isDownloaded = true)
        val isUpdateDownloaded = checkFlexibleUpdateDownloadStateUseCase().first()
        assertThat(isUpdateDownloaded).isTrue()
    }
}
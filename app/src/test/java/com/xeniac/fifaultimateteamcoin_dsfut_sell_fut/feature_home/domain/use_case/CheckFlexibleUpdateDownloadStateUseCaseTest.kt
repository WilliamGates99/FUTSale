package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeMiscellaneousDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories.FakeAppUpdateRepositoryImpl
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
    private lateinit var fakeAppUpdateRepository: FakeAppUpdateRepositoryImpl
    private lateinit var checkFlexibleUpdateDownloadStateUseCase: CheckFlexibleUpdateDownloadStateUseCase

    @Before
    fun setUp() {
        fakeMiscellaneousDataStoreRepositoryImpl = FakeMiscellaneousDataStoreRepositoryImpl()
        fakeAppUpdateRepository = FakeAppUpdateRepositoryImpl(
            miscellaneousDataStoreRepository = fakeMiscellaneousDataStoreRepositoryImpl
        )

        checkFlexibleUpdateDownloadStateUseCase = CheckFlexibleUpdateDownloadStateUseCase(
            appUpdateRepository = fakeAppUpdateRepository
        )
    }

    @Test
    fun checkFlexibleUpdateDownloadStateWithNoUpdateDownloaded_returnsNothing() = runTest {
        fakeAppUpdateRepository.isFlexibleUpdateDownloaded(isDownloaded = false)
        val isUpdateDownloaded = checkFlexibleUpdateDownloadStateUseCase().firstOrNull()
        assertThat(isUpdateDownloaded).isNull()
    }

    @Test
    fun checkFlexibleUpdateDownloadStateWithUpdateDownloaded_returnsTrue() = runTest {
        fakeAppUpdateRepository.isFlexibleUpdateDownloaded(isDownloaded = true)
        val isUpdateDownloaded = checkFlexibleUpdateDownloadStateUseCase().first()
        assertThat(isUpdateDownloaded).isTrue()
    }
}
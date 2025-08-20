package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeMiscellaneousDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories.FakeAppUpdateRepositoryImpl
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
class CheckFlexibleUpdateDownloadStateUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeMiscellaneousDataStoreRepositoryImpl: FakeMiscellaneousDataStoreRepositoryImpl
    private lateinit var fakeAppUpdateRepositoryImpl: FakeAppUpdateRepositoryImpl
    private lateinit var checkFlexibleUpdateDownloadStateUseCase: CheckFlexibleUpdateDownloadStateUseCase

    @Before
    fun setUp() {
        fakeMiscellaneousDataStoreRepositoryImpl = FakeMiscellaneousDataStoreRepositoryImpl()
        fakeAppUpdateRepositoryImpl = FakeAppUpdateRepositoryImpl(
            miscellaneousDataStoreRepository = fakeMiscellaneousDataStoreRepositoryImpl
        )
        checkFlexibleUpdateDownloadStateUseCase = CheckFlexibleUpdateDownloadStateUseCase(
            appUpdateRepository = fakeAppUpdateRepositoryImpl
        )
    }

    @Test
    fun checkFlexibleUpdateDownloadStateWithNoUpdateDownloaded_returnsNothing() = runTest {
        fakeAppUpdateRepositoryImpl.isFlexibleUpdateDownloaded(isDownloaded = false)
        checkFlexibleUpdateDownloadStateUseCase().onEach { isUpdateDownloaded ->
            assertThat(isUpdateDownloaded).isNull()
        }
    }

    @Test
    fun checkFlexibleUpdateDownloadStateWithUpdateDownloaded_returnsTrue() = runTest {
        fakeAppUpdateRepositoryImpl.isFlexibleUpdateDownloaded(isDownloaded = true)
        checkFlexibleUpdateDownloadStateUseCase().onEach { isUpdateDownloaded ->
            assertThat(isUpdateDownloaded).isTrue()
        }
    }
}
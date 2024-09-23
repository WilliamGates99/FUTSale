package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeMiscellaneousDataStoreRepositoryImpl
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
class CheckForAppUpdatesUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeMiscellaneousDataStoreRepositoryImpl: FakeMiscellaneousDataStoreRepositoryImpl
    private lateinit var fakeHomeRepository: FakeHomeRepositoryImpl
    private lateinit var checkForAppUpdatesUseCase: CheckForAppUpdatesUseCase

    @Before
    fun setUp() {
        fakeMiscellaneousDataStoreRepositoryImpl = FakeMiscellaneousDataStoreRepositoryImpl()
        fakeHomeRepository = FakeHomeRepositoryImpl(
            miscellaneousDataStoreRepository = fakeMiscellaneousDataStoreRepositoryImpl
        )

        checkForAppUpdatesUseCase = CheckForAppUpdatesUseCase(
            homeRepository = fakeHomeRepository
        )
    }

    @Test
    fun checkForAppUpdatesWithNoUpdateAvailable_returnsNull() = runTest {
        fakeHomeRepository.isAppUpdateAvailable(isAvailable = false)
        val appUpdateInfo = checkForAppUpdatesUseCase().first()
        assertThat(appUpdateInfo).isNull()
    }

    @Test
    fun checkForAppUpdatesWithUpdateAvailable_returnsAppUpdateInfo() = runTest {
        fakeHomeRepository.isAppUpdateAvailable(isAvailable = true)
        val appUpdateInfo = checkForAppUpdatesUseCase().first()
        assertThat(appUpdateInfo).isNotNull()
    }
}
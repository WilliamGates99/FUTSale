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
class CheckForAppUpdatesUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeMiscellaneousDataStoreRepositoryImpl: FakeMiscellaneousDataStoreRepositoryImpl
    private lateinit var fakeAppUpdateRepositoryImpl: FakeAppUpdateRepositoryImpl
    private lateinit var checkForAppUpdatesUseCase: CheckForAppUpdatesUseCase

    @Before
    fun setUp() {
        fakeMiscellaneousDataStoreRepositoryImpl = FakeMiscellaneousDataStoreRepositoryImpl()
        fakeAppUpdateRepositoryImpl = FakeAppUpdateRepositoryImpl(
            miscellaneousDataStoreRepository = fakeMiscellaneousDataStoreRepositoryImpl
        )
        checkForAppUpdatesUseCase = CheckForAppUpdatesUseCase(
            appUpdateRepository = fakeAppUpdateRepositoryImpl
        )
    }

    @Test
    fun checkForAppUpdatesWithNoUpdateAvailable_returnsNull() = runTest {
        fakeAppUpdateRepositoryImpl.isAppUpdateAvailable(isAvailable = false)
        checkForAppUpdatesUseCase().onEach { appUpdateInfo ->
            assertThat(appUpdateInfo).isNull()
        }
    }

    @Test
    fun checkForAppUpdatesWithUpdateAvailable_returnsAppUpdateInfo() = runTest {
        fakeAppUpdateRepositoryImpl.isAppUpdateAvailable(isAvailable = true)
        checkForAppUpdatesUseCase().onEach { appUpdateInfo ->
            assertThat(appUpdateInfo).isNotNull()
        }
    }
}
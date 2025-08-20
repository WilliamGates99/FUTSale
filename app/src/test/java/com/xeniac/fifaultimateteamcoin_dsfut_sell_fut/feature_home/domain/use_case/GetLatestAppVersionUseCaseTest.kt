package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeMiscellaneousDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories.FakeAppUpdateRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.models.LatestAppUpdateInfo
import io.ktor.http.HttpStatusCode
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
class GetLatestAppVersionUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeMiscellaneousDataStoreRepositoryImpl: FakeMiscellaneousDataStoreRepositoryImpl
    private lateinit var fakeAppUpdateRepositoryImpl: FakeAppUpdateRepositoryImpl
    private lateinit var getLatestAppVersionUseCase: GetLatestAppVersionUseCase

    @Before
    fun setUp() {
        fakeMiscellaneousDataStoreRepositoryImpl = FakeMiscellaneousDataStoreRepositoryImpl()
        fakeAppUpdateRepositoryImpl = FakeAppUpdateRepositoryImpl(
            miscellaneousDataStoreRepository = fakeMiscellaneousDataStoreRepositoryImpl
        )
        getLatestAppVersionUseCase = GetLatestAppVersionUseCase(
            appUpdateRepository = fakeAppUpdateRepositoryImpl
        )
    }

    @Test
    fun getLatestAppVersionWithUnavailableNetwork_returnsError() = runTest {
        fakeAppUpdateRepositoryImpl.isNetworkAvailable(isAvailable = false)
        getLatestAppVersionUseCase().onEach { result ->
            assertThat(result).isInstanceOf(Result.Error::class.java)
        }
    }

    @Test
    fun getLatestAppVersionWithNoneOkStatusCode_returnsError() = runTest {
        fakeAppUpdateRepositoryImpl.setGetLatestAppVersionHttpStatusCode(
            httpStatusCode = HttpStatusCode.RequestTimeout
        )
        getLatestAppVersionUseCase().onEach { result ->
            assertThat(result).isInstanceOf(Result.Error::class.java)
        }
    }

    @Test
    fun getLatestAppVersionWhenAppIsUpdated_returnsNull() = runTest {
        getLatestAppVersionUseCase().onEach { result ->
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).data).isNull()
        }
    }

    @Test
    fun getLatestAppVersionWhenAppIsOutdated_returnsNewVersionName() = runTest {
        val latestAppUpdateInfo = LatestAppUpdateInfo(
            versionCode = BuildConfig.VERSION_CODE + 1,
            versionName = BuildConfig.VERSION_NAME + "-test"
        )

        fakeAppUpdateRepositoryImpl.setLatestAppVersion(
            latestAppUpdateInfo = latestAppUpdateInfo
        )

        getLatestAppVersionUseCase().onEach { result ->
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).data).isEqualTo(latestAppUpdateInfo)
        }
    }

    @Test
    fun getLatestAppVersionWhenAppIsOutdatedAndUpdateDialogIsShownMoreThan2TimesToday_returnsNull() =
        runTest {
            fakeMiscellaneousDataStoreRepositoryImpl.setShouldStoreTodayDateTime(true)
            fakeMiscellaneousDataStoreRepositoryImpl.storeAppUpdateDialogShowDateTime()
            fakeMiscellaneousDataStoreRepositoryImpl.storeAppUpdateDialogShowCount(count = 3)

            val latestAppUpdateInfo = LatestAppUpdateInfo(
                versionCode = BuildConfig.VERSION_CODE + 1,
                versionName = BuildConfig.VERSION_NAME + "-test"
            )

            fakeAppUpdateRepositoryImpl.setLatestAppVersion(
                latestAppUpdateInfo = latestAppUpdateInfo
            )

            getLatestAppVersionUseCase().onEach { result ->
                assertThat(result).isInstanceOf(Result.Success::class.java)
                assertThat((result as Result.Success).data).isNull()
            }
        }

    @Test
    fun getLatestAppVersionWhenAppIsOutdatedAndUpdateDialogIsShownMoreThan2TimesYesterday_returnsNewVersionName() =
        runTest {
            fakeMiscellaneousDataStoreRepositoryImpl.setShouldStoreTodayDateTime(false)
            fakeMiscellaneousDataStoreRepositoryImpl.storeAppUpdateDialogShowDateTime()
            fakeMiscellaneousDataStoreRepositoryImpl.storeAppUpdateDialogShowCount(count = 3)

            val latestAppUpdateInfo = LatestAppUpdateInfo(
                versionCode = BuildConfig.VERSION_CODE + 1,
                versionName = BuildConfig.VERSION_NAME + "-test"
            )

            fakeAppUpdateRepositoryImpl.setLatestAppVersion(
                latestAppUpdateInfo = latestAppUpdateInfo
            )

            getLatestAppVersionUseCase().onEach { result ->
                assertThat(result).isInstanceOf(Result.Success::class.java)
                assertThat((result as Result.Success).data).isEqualTo(latestAppUpdateInfo)
            }
        }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeMiscellaneousDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories.FakeAppUpdateRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.models.LatestAppUpdateInfo
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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
    private lateinit var fakeAppUpdateRepository: FakeAppUpdateRepositoryImpl
    private lateinit var getLatestAppVersionUseCase: GetLatestAppVersionUseCase

    @Before
    fun setUp() {
        fakeMiscellaneousDataStoreRepositoryImpl = FakeMiscellaneousDataStoreRepositoryImpl()
        fakeAppUpdateRepository = FakeAppUpdateRepositoryImpl(
            miscellaneousDataStoreRepository = fakeMiscellaneousDataStoreRepositoryImpl
        )

        getLatestAppVersionUseCase = GetLatestAppVersionUseCase(
            appUpdateRepository = fakeAppUpdateRepository
        )
    }

    @Test
    fun getLatestAppVersionWithUnavailableNetwork_returnsError() = runBlocking {
        fakeAppUpdateRepository.isNetworkAvailable(isAvailable = false)

        val getLatestAppVersionResult = getLatestAppVersionUseCase().first()

        assertThat(getLatestAppVersionResult).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getLatestAppVersionWithNoneOkStatusCode_returnsError() = runBlocking {
        fakeAppUpdateRepository.setGetLatestAppVersionHttpStatusCode(HttpStatusCode.RequestTimeout)

        val getLatestAppVersionResult = getLatestAppVersionUseCase().first()

        assertThat(getLatestAppVersionResult).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getLatestAppVersionWhenAppIsUpdated_returnsNull() = runBlocking {
        val getLatestAppVersionResult = getLatestAppVersionUseCase().first()

        assertThat(getLatestAppVersionResult).isInstanceOf(Result.Success::class.java)
        assertThat((getLatestAppVersionResult as Result.Success).data).isNull()
    }

    @Test
    fun getLatestAppVersionWhenAppIsOutdated_returnsNewVersionName() = runBlocking {
        val latestAppUpdateInfo = LatestAppUpdateInfo(
            versionCode = BuildConfig.VERSION_CODE + 1,
            versionName = BuildConfig.VERSION_NAME + "-test"
        )

        fakeAppUpdateRepository.setLatestAppVersion(
            latestAppUpdateInfo = latestAppUpdateInfo
        )

        val getLatestAppVersionResult = getLatestAppVersionUseCase().first()

        assertThat(getLatestAppVersionResult).isInstanceOf(Result.Success::class.java)
        assertThat((getLatestAppVersionResult as Result.Success).data).isEqualTo(latestAppUpdateInfo)
    }

    @Test
    fun getLatestAppVersionWhenAppIsOutdatedAndUpdateDialogIsShownMoreThan2TimesToday_returnsNull() =
        runBlocking {
            fakeMiscellaneousDataStoreRepositoryImpl.setShouldStoreTodayDate(true)
            fakeMiscellaneousDataStoreRepositoryImpl.storeAppUpdateDialogShowEpochDays()
            fakeMiscellaneousDataStoreRepositoryImpl.storeAppUpdateDialogShowCount(3)

            val latestAppUpdateInfo = LatestAppUpdateInfo(
                versionCode = BuildConfig.VERSION_CODE + 1,
                versionName = BuildConfig.VERSION_NAME + "-test"
            )

            fakeAppUpdateRepository.setLatestAppVersion(
                latestAppUpdateInfo = latestAppUpdateInfo
            )

            val getLatestAppVersionResult = getLatestAppVersionUseCase().first()

            assertThat(getLatestAppVersionResult).isInstanceOf(Result.Success::class.java)
            assertThat((getLatestAppVersionResult as Result.Success).data).isNull()
        }

    @Test
    fun getLatestAppVersionWhenAppIsOutdatedAndUpdateDialogIsShownMoreThan2TimesYesterday_returnsNewVersionName() =
        runBlocking {
            fakeMiscellaneousDataStoreRepositoryImpl.setShouldStoreTodayDate(false)
            fakeMiscellaneousDataStoreRepositoryImpl.storeAppUpdateDialogShowEpochDays()
            fakeMiscellaneousDataStoreRepositoryImpl.storeAppUpdateDialogShowCount(3)

            val latestAppUpdateInfo = LatestAppUpdateInfo(
                versionCode = BuildConfig.VERSION_CODE + 1,
                versionName = BuildConfig.VERSION_NAME + "-test"
            )

            fakeAppUpdateRepository.setLatestAppVersion(
                latestAppUpdateInfo = latestAppUpdateInfo
            )

            val getLatestAppVersionResult = getLatestAppVersionUseCase().first()

            assertThat(getLatestAppVersionResult).isInstanceOf(Result.Success::class.java)
            assertThat((getLatestAppVersionResult as Result.Success).data)
                .isEqualTo(latestAppUpdateInfo)
        }
}
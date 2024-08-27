package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakePreferencesRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.data.repositories.FakeHomeRepositoryImpl
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private lateinit var fakePreferencesRepositoryImpl: FakePreferencesRepositoryImpl
    private lateinit var fakeHomeRepositoryImpl: FakeHomeRepositoryImpl
    private lateinit var getLatestAppVersionUseCase: GetLatestAppVersionUseCase

    @Before
    fun setUp() {
        fakePreferencesRepositoryImpl = FakePreferencesRepositoryImpl()
        fakeHomeRepositoryImpl = FakeHomeRepositoryImpl(
            preferencesRepository = { fakePreferencesRepositoryImpl }
        )

        getLatestAppVersionUseCase = GetLatestAppVersionUseCase(
            homeRepository = fakeHomeRepositoryImpl
        )
    }

    @Test
    fun getLatestAppVersionWithUnavailableNetwork_returnsError() = runBlocking {
        fakeHomeRepositoryImpl.isNetworkAvailable(isAvailable = false)

        val getLatestAppVersionResult = getLatestAppVersionUseCase()

        assertThat(getLatestAppVersionResult).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getLatestAppVersionWithNoneOkStatusCode_returnsError() = runBlocking {
        fakeHomeRepositoryImpl.setGetLatestAppVersionHttpStatusCode(HttpStatusCode.RequestTimeout)

        val getLatestAppVersionResult = getLatestAppVersionUseCase()

        assertThat(getLatestAppVersionResult).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun getLatestAppVersionWhenAppIsUpdated_returnsNull() = runBlocking {
        val getLatestAppVersionResult = getLatestAppVersionUseCase()

        assertThat(getLatestAppVersionResult).isInstanceOf(Result.Success::class.java)
        assertThat((getLatestAppVersionResult as Result.Success).data).isNull()
    }

    @Test
    fun getLatestAppVersionWhenAppIsOutdated_returnsNewVersionName() = runBlocking {
        val newVersionCode = BuildConfig.VERSION_CODE + 1
        val newVersionName = BuildConfig.VERSION_NAME + "-test"

        fakeHomeRepositoryImpl.setLatestAppVersion(
            versionCode = newVersionCode,
            versionName = newVersionName
        )

        val getLatestAppVersionResult = getLatestAppVersionUseCase()

        assertThat(getLatestAppVersionResult).isInstanceOf(Result.Success::class.java)
        assertThat((getLatestAppVersionResult as Result.Success).data).isEqualTo(newVersionName)
    }

    @Test
    fun getLatestAppVersionWhenAppIsOutdatedAndUpdateDialogIsShownMoreThan2TimesToday_returnsNull() =
        runBlocking {
            fakePreferencesRepositoryImpl.setShouldStoreTodayDate(true)
            fakePreferencesRepositoryImpl.storeAppUpdateDialogShowEpochDays()
            fakePreferencesRepositoryImpl.setAppUpdateDialogShowCount(3)

            val newVersionCode = BuildConfig.VERSION_CODE + 1
            val newVersionName = BuildConfig.VERSION_NAME + "-test"

            fakeHomeRepositoryImpl.setLatestAppVersion(
                versionCode = newVersionCode,
                versionName = newVersionName
            )

            val getLatestAppVersionResult = getLatestAppVersionUseCase()

            assertThat(getLatestAppVersionResult).isInstanceOf(Result.Success::class.java)
            assertThat((getLatestAppVersionResult as Result.Success).data).isNull()
        }

    @Test
    fun getLatestAppVersionWhenAppIsOutdatedAndUpdateDialogIsShownMoreThan2TimesYesterday_returnsNewVersionName() =
        runBlocking {
            fakePreferencesRepositoryImpl.setShouldStoreTodayDate(false)
            fakePreferencesRepositoryImpl.storeAppUpdateDialogShowEpochDays()
            fakePreferencesRepositoryImpl.setAppUpdateDialogShowCount(3)

            val newVersionCode = BuildConfig.VERSION_CODE + 1
            val newVersionName = BuildConfig.VERSION_NAME + "-test"

            fakeHomeRepositoryImpl.setLatestAppVersion(
                versionCode = newVersionCode,
                versionName = newVersionName
            )

            val getLatestAppVersionResult = getLatestAppVersionUseCase()

            assertThat(getLatestAppVersionResult).isInstanceOf(Result.Success::class.java)
            assertThat((getLatestAppVersionResult as Result.Success).data).isEqualTo(newVersionName)
        }
}
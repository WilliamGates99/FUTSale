package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeSettingsDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
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
class StoreCurrentAppThemeUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeSettingsDataStoreRepositoryImpl: FakeSettingsDataStoreRepositoryImpl
    private lateinit var storeCurrentAppThemeUseCase: StoreCurrentAppThemeUseCase
    private lateinit var getCurrentAppThemeUseCase: GetCurrentAppThemeUseCase

    @Before
    fun setUp() {
        fakeSettingsDataStoreRepositoryImpl = FakeSettingsDataStoreRepositoryImpl()
        storeCurrentAppThemeUseCase = StoreCurrentAppThemeUseCase(
            settingsDataStoreRepository = fakeSettingsDataStoreRepositoryImpl
        )
        getCurrentAppThemeUseCase = GetCurrentAppThemeUseCase(
            settingsDataStoreRepository = fakeSettingsDataStoreRepositoryImpl
        )
    }

    @Test
    fun storeCurrentAppTheme_returnsSuccess() = runTest {
        val testValue = AppTheme.Dark
        val result = storeCurrentAppThemeUseCase(testValue)
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun storeCurrentAppTheme_returnsNewAppTheme() = runTest {
        val testValue = AppTheme.Dark
        storeCurrentAppThemeUseCase(testValue)

        val appTheme = getCurrentAppThemeUseCase().first()
        assertThat(appTheme).isEqualTo(testValue)
    }
}
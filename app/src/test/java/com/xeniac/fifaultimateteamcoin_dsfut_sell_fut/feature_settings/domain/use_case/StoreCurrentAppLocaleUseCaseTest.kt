package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeSettingsDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppLocale
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class StoreCurrentAppLocaleUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeSettingsDataStoreRepositoryImpl: FakeSettingsDataStoreRepositoryImpl
    private lateinit var storeCurrentAppLocaleUseCase: StoreCurrentAppLocaleUseCase
    private lateinit var getCurrentAppLocaleUseCase: GetCurrentAppLocaleUseCase

    @Before
    fun setUp() {
        fakeSettingsDataStoreRepositoryImpl = FakeSettingsDataStoreRepositoryImpl()
        storeCurrentAppLocaleUseCase = StoreCurrentAppLocaleUseCase(
            settingsDataStoreRepository = fakeSettingsDataStoreRepositoryImpl
        )
        getCurrentAppLocaleUseCase = GetCurrentAppLocaleUseCase(
            settingsDataStoreRepository = fakeSettingsDataStoreRepositoryImpl
        )
    }

    @Test
    fun storeCurrentAppLocale_returnsIsActivityRestartNeeded() = runTest {
        val testValue = AppLocale.FarsiIR

        val isActivityRestartNeeded = fakeSettingsDataStoreRepositoryImpl.isActivityRestartNeeded(
            newLocale = testValue
        )

        val result = storeCurrentAppLocaleUseCase(testValue)
        assertThat(result).isEqualTo(isActivityRestartNeeded)
    }

    @Test
    fun storeCurrentAppLocale_returnsNewAppLocale() = runTest {
        val testValue = AppLocale.FarsiIR
        storeCurrentAppLocaleUseCase(testValue)

        val appLocale = getCurrentAppLocaleUseCase()
        assertThat(appLocale).isEqualTo(testValue)
    }
}
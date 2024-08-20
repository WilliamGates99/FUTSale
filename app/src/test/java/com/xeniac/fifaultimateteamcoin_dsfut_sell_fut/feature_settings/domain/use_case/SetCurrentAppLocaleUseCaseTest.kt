package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakePreferencesRepositoryImpl
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
class SetCurrentAppLocaleUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePreferencesRepository: FakePreferencesRepositoryImpl
    private lateinit var setCurrentAppLocaleUseCase: SetCurrentAppLocaleUseCase
    private lateinit var getCurrentAppLocaleUseCase: GetCurrentAppLocaleUseCase

    @Before
    fun setUp() {
        fakePreferencesRepository = FakePreferencesRepositoryImpl()
        setCurrentAppLocaleUseCase = SetCurrentAppLocaleUseCase(
            preferencesRepository = fakePreferencesRepository
        )
        getCurrentAppLocaleUseCase = GetCurrentAppLocaleUseCase(
            preferencesRepository = fakePreferencesRepository
        )
    }

    @Test
    fun setCurrentAppLocale_returnsIsActivityRestartNeeded() = runTest {
        val testValue = AppLocale.FarsiIR

        val isActivityRestartNeeded = fakePreferencesRepository.isActivityRestartNeeded(
            newLayoutDirection = testValue.layoutDirection
        )

        val result = setCurrentAppLocaleUseCase(testValue)
        assertThat(result).isEqualTo(isActivityRestartNeeded)
    }

    @Test
    fun setCurrentAppLocale_returnsNewAppLocale() = runTest {
        val testValue = AppLocale.FarsiIR
        setCurrentAppLocaleUseCase(testValue)

        val appLocale = getCurrentAppLocaleUseCase()
        assertThat(appLocale).isEqualTo(testValue)
    }
}
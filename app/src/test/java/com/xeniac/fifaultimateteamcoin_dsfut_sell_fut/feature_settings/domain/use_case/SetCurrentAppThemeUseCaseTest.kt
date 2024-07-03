package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakePreferencesRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.AppTheme
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class SetCurrentAppThemeUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePreferencesRepository: FakePreferencesRepositoryImpl
    private lateinit var setCurrentAppThemeUseCase: SetCurrentAppThemeUseCase
    private lateinit var getCurrentSettingsUseCase: GetCurrentSettingsUseCase

    @Before
    fun setUp() {
        fakePreferencesRepository = FakePreferencesRepositoryImpl()
        setCurrentAppThemeUseCase = SetCurrentAppThemeUseCase(
            preferencesRepository = fakePreferencesRepository
        )
        getCurrentSettingsUseCase = GetCurrentSettingsUseCase(
            preferencesRepository = fakePreferencesRepository
        )
    }

    @Test
    fun setIsNotificationSoundEnabled_returnsSuccess() = runTest {
        val testValue = AppTheme.Dark
        val result = setCurrentAppThemeUseCase(testValue)
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun setIsNotificationSoundEnabled_returnsNewIsNotificationSoundEnabled() = runTest {
        val testValue = AppTheme.Dark
        setCurrentAppThemeUseCase(testValue)

        val appTheme = getCurrentSettingsUseCase().appTheme
        assertThat(appTheme).isEqualTo(testValue)
    }
}
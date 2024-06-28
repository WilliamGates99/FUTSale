package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_settings.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakePreferencesRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class SetIsNotificationVibrateEnabledUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePreferencesRepository: FakePreferencesRepositoryImpl
    private lateinit var setIsNotificationVibrateEnabledUseCase: SetIsNotificationVibrateEnabledUseCase
    private lateinit var getCurrentSettingsUseCase: GetCurrentSettingsUseCase

    @Before
    fun setUp() {
        fakePreferencesRepository = FakePreferencesRepositoryImpl()
        setIsNotificationVibrateEnabledUseCase = SetIsNotificationVibrateEnabledUseCase(
            preferencesRepository = fakePreferencesRepository
        )
        getCurrentSettingsUseCase = GetCurrentSettingsUseCase(
            preferencesRepository = fakePreferencesRepository
        )
    }

    @Test
    fun setIsNotificationVibrateEnabled_returnsNewIsNotificationVibrateEnabled() = runTest {
        val testValue = false
        setIsNotificationVibrateEnabledUseCase(testValue)

        val isNotificationVibrateEnabled = getCurrentSettingsUseCase().isNotificationVibrateEnabled
        assertThat(isNotificationVibrateEnabled).isEqualTo(testValue)
    }
}
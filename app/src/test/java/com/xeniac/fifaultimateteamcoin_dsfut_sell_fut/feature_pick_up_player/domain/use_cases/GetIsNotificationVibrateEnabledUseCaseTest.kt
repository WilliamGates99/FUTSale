package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeSettingsDataStoreRepositoryImpl
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
class GetIsNotificationVibrateEnabledUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeSettingsDataStoreRepositoryImpl: FakeSettingsDataStoreRepositoryImpl
    private lateinit var getIsNotificationVibrateEnabledUseCase: GetIsNotificationVibrateEnabledUseCase

    @Before
    fun setUp() {
        fakeSettingsDataStoreRepositoryImpl = FakeSettingsDataStoreRepositoryImpl()
        getIsNotificationVibrateEnabledUseCase = GetIsNotificationVibrateEnabledUseCase(
            settingsDataStoreRepository = fakeSettingsDataStoreRepositoryImpl
        )
    }

    @Test
    fun getIsNotificationVibrateEnabled_returnsCurrentIsNotificationVibrateEnabled() = runTest {
        getIsNotificationVibrateEnabledUseCase().onEach { isNotificationVibrateEnabled ->
            assertThat(isNotificationVibrateEnabled).isEqualTo(
                fakeSettingsDataStoreRepositoryImpl.isNotificationVibrateEnabled.first()
            )
        }
    }
}
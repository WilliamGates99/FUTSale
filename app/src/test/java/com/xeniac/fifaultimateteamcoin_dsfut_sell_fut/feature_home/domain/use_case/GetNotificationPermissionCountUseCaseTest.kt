package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeSettingsDataStoreRepositoryImpl
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
class GetNotificationPermissionCountUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeSettingsDataStoreRepositoryImpl: FakeSettingsDataStoreRepositoryImpl
    private lateinit var getNotificationPermissionCountUseCase: GetNotificationPermissionCountUseCase

    @Before
    fun setUp() {
        fakeSettingsDataStoreRepositoryImpl = FakeSettingsDataStoreRepositoryImpl()
        getNotificationPermissionCountUseCase = GetNotificationPermissionCountUseCase(
            settingsDataStoreRepository = fakeSettingsDataStoreRepositoryImpl
        )
    }

    @Test
    fun getNotificationPermissionCount_returnsCurrentNotificationPermissionCountValue() = runTest {
        val currentNotificationPermissionCount = getNotificationPermissionCountUseCase().first()
        assertThat(currentNotificationPermissionCount).isEqualTo(
            fakeSettingsDataStoreRepositoryImpl.notificationPermissionCount
        )
    }
}
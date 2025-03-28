package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeMiscellaneousDataStoreRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
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
class StoreSelectedRateAppOptionUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeMiscellaneousDataStoreRepositoryImpl: FakeMiscellaneousDataStoreRepositoryImpl
    private lateinit var storeSelectedRateAppOptionUseCase: StoreSelectedRateAppOptionUseCase
    private lateinit var getSelectedRateAppOptionUseCase: GetSelectedRateAppOptionUseCase

    @Before
    fun setUp() {
        fakeMiscellaneousDataStoreRepositoryImpl = FakeMiscellaneousDataStoreRepositoryImpl()
        storeSelectedRateAppOptionUseCase = StoreSelectedRateAppOptionUseCase(
            miscellaneousDataStoreRepository = fakeMiscellaneousDataStoreRepositoryImpl
        )
        getSelectedRateAppOptionUseCase = GetSelectedRateAppOptionUseCase(
            miscellaneousDataStoreRepository = fakeMiscellaneousDataStoreRepositoryImpl
        )
    }

    @Test
    fun storeNotificationPermissionCount_returnsNewNotificationPermissionCount() = runTest {
        val testValue = RateAppOption.RATE_NOW
        storeSelectedRateAppOptionUseCase(testValue).first()

        val selectedRateAppOption = getSelectedRateAppOptionUseCase().first()
        assertThat(selectedRateAppOption).isEqualTo(testValue)
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_home.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakePreferencesRepositoryImpl
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private lateinit var fakePreferencesRepository: FakePreferencesRepositoryImpl
    private lateinit var storeSelectedRateAppOptionUseCase: StoreSelectedRateAppOptionUseCase
    private lateinit var getSelectedRateAppOptionUseCase: GetSelectedRateAppOptionUseCase

    @Before
    fun setUp() {
        fakePreferencesRepository = FakePreferencesRepositoryImpl()
        storeSelectedRateAppOptionUseCase = StoreSelectedRateAppOptionUseCase(
            preferencesRepository = fakePreferencesRepository
        )
        getSelectedRateAppOptionUseCase = GetSelectedRateAppOptionUseCase(
            preferencesRepository = fakePreferencesRepository
        )
    }

    @Test
    fun storeNotificationPermissionCount_returnsNewNotificationPermissionCount() = runTest {
        val testValue = RateAppOption.RATE_NOW
        storeSelectedRateAppOptionUseCase(testValue)

        val selectedRateAppOption = getSelectedRateAppOptionUseCase()
        assertThat(selectedRateAppOption).isEqualTo(testValue)
    }
}
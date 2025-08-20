package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.FakeCountDownTimerRepositoryImpl
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
class StartCountDownTimerUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeCountDownTimerRepositoryImpl: FakeCountDownTimerRepositoryImpl
    private lateinit var startCountDownTimerUseCase: StartCountDownTimerUseCase

    @Before
    fun setUp() {
        fakeCountDownTimerRepositoryImpl = FakeCountDownTimerRepositoryImpl()
        startCountDownTimerUseCase = StartCountDownTimerUseCase(
            countDownTimerRepository = fakeCountDownTimerRepositoryImpl
        )
    }

    @Test
    fun startCountDownTimerWithExpiredPlayer_returnsZero() = runTest {
        val expiryTime = -10000L // -10 seconds
        startCountDownTimerUseCase(expiryTimeInMs = expiryTime).onEach { timerValueInSeconds ->
            assertThat(timerValueInSeconds).isEqualTo(0)
        }
    }

    @Test
    fun startCountDownTimerWithNonExpiredPlayer_returnsTimerValueInSeconds() = runTest {
        val expiryTimeInMs = 10000L // 10 seconds
        val expiryTimeInSeconds = expiryTimeInMs / 1000

        startCountDownTimerUseCase(expiryTimeInMs = expiryTimeInMs).onEach { timerValueInSeconds ->
            assertThat(timerValueInSeconds).isEqualTo(expiryTimeInSeconds)
        }
    }
}
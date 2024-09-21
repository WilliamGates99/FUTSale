package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.data.repositories.FakeHistoryRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class ObservePlayerUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeHistoryRepositoryImpl: FakeHistoryRepositoryImpl
    private lateinit var observePlayerUseCase: ObservePlayerUseCase

    @Before
    fun setUp() {
        fakeHistoryRepositoryImpl = FakeHistoryRepositoryImpl()
        observePlayerUseCase = ObservePlayerUseCase(
            historyRepository = fakeHistoryRepositoryImpl
        )
    }

    @Test
    fun observePlayerUseCaseWithNoPlayersInHistory_returnsNull() = runTest {
        val player = observePlayerUseCase(playerId = 5).firstOrNull()
        assertThat(player).isNull()
    }

    @Test
    fun observePickedUpPlayer_returnsPlayerWithTheSameId() = runTest {
        fakeHistoryRepositoryImpl.addDummyPlayersToHistory()

        val playerId = 5L
        val player = observePlayerUseCase(playerId).first()

        assertThat(player.id).isEqualTo(playerId)
    }

    @Test
    fun observePickedUpPlayerWithInvalidId_returnsNull() = runTest {
        fakeHistoryRepositoryImpl.addDummyPlayersToHistory()
        val player = observePlayerUseCase(playerId = 100L).firstOrNull()
        assertThat(player).isNull()
    }
}
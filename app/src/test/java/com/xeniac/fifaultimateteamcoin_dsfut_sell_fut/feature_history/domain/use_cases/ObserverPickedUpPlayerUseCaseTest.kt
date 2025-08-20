package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.data.repositories.FakeHistoryRepositoryImpl
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
class ObserverPickedUpPlayerUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeHistoryRepositoryImpl: FakeHistoryRepositoryImpl
    private lateinit var observerPickedUpPlayerUseCase: ObserverPickedUpPlayerUseCase

    @Before
    fun setUp() {
        fakeHistoryRepositoryImpl = FakeHistoryRepositoryImpl()
        observerPickedUpPlayerUseCase = ObserverPickedUpPlayerUseCase(
            historyRepository = fakeHistoryRepositoryImpl
        )
    }

    @Test
    fun observePlayerUseCaseWithNoPlayersInHistory_returnsNull() = runTest {
        observerPickedUpPlayerUseCase(
            playerId = 5
        ).onEach { player ->
            assertThat(player).isNull()
        }
    }

    @Test
    fun observePickedUpPlayer_returnsPlayerWithTheSameId() = runTest {
        fakeHistoryRepositoryImpl.addDummyPlayersToHistory()

        val playerId = 5L
        observerPickedUpPlayerUseCase(
            playerId = playerId
        ).onEach { player ->
            assertThat(player.id).isEqualTo(playerId)
        }
    }

    @Test
    fun observePickedUpPlayerWithInvalidId_returnsNull() = runTest {
        fakeHistoryRepositoryImpl.addDummyPlayersToHistory()

        observerPickedUpPlayerUseCase(
            playerId = 100L
        ).onEach { player ->
            assertThat(player).isNull()
        }
    }
}
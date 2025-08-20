package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.FakePickedUpPlayersRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class ObservePickedUpPlayerUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePickedUpPlayersRepositoryImpl: FakePickedUpPlayersRepositoryImpl
    private lateinit var observePickedUpPlayerUseCase: ObservePickedUpPlayerUseCase

    @Before
    fun setUp() {
        fakePickedUpPlayersRepositoryImpl = FakePickedUpPlayersRepositoryImpl()
        observePickedUpPlayerUseCase = ObservePickedUpPlayerUseCase(
            pickedUpPlayersRepository = fakePickedUpPlayersRepositoryImpl
        )
    }

    @Test
    fun observePickedUpPlayerWithNoPlayers_returnsNull() = runTest {
        val pickedUpPlayer = observePickedUpPlayerUseCase(playerId = 5).firstOrNull()
        assertThat(pickedUpPlayer).isNull()
    }

    @Test
    fun observePickedUpPlayer_returnsPlayerWithTheSameId() = runTest {
        fakePickedUpPlayersRepositoryImpl.addDummyPlayersToLatestPlayers()

        val playerId = 5L

        observePickedUpPlayerUseCase(
            playerId = playerId
        ).onEach { pickedUpPlayer ->
            assertThat(pickedUpPlayer.id).isEqualTo(playerId)
        }
    }

    @Test
    fun observePickedUpPlayerWithInvalidId_returnsNull() = runTest {
        fakePickedUpPlayersRepositoryImpl.addDummyPlayersToLatestPlayers()

        val pickedUpPlayer = observePickedUpPlayerUseCase(
            playerId = 100L
        ).firstOrNull()

        assertThat(pickedUpPlayer).isNull()
    }
}
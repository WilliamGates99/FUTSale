package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.MainCoroutineRule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories.FakePickedUpPlayersRepositoryImpl
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
class ObserveLatestPickedUpPlayersUseCaseTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakePickedUpPlayersRepositoryImpl: FakePickedUpPlayersRepositoryImpl
    private lateinit var observeLatestPickedUpPlayersUseCase: ObserveLatestPickedUpPlayersUseCase

    @Before
    fun setUp() {
        fakePickedUpPlayersRepositoryImpl = FakePickedUpPlayersRepositoryImpl()
        observeLatestPickedUpPlayersUseCase = ObserveLatestPickedUpPlayersUseCase(
            pickedUpPlayersRepository = fakePickedUpPlayersRepositoryImpl
        )
    }

    @Test
    fun observeLatestPickedPlayersWithNoPlayers_returnsEmptyPlayersList() = runTest {
        observeLatestPickedUpPlayersUseCase().onEach { latestPickedPlayers ->
            assertThat(latestPickedPlayers).isEmpty()
        }
    }

    @Test
    fun observeLatestPickedPlayers_returnsPlayersListInDescendingOrder() = runTest {
        fakePickedUpPlayersRepositoryImpl.addDummyPlayersToLatestPlayers()

        observeLatestPickedUpPlayersUseCase().onEach { latestPickedPlayers ->
            assertThat(latestPickedPlayers).isNotEmpty()

            for (i in 0..latestPickedPlayers.size - 2) {
                assertThat(latestPickedPlayers[i].pickUpTimeInMs)
                    .isAtLeast(latestPickedPlayers[i + 1].pickUpTimeInMs)
            }
        }
    }
}
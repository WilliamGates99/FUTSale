package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.testing.asSnapshot
import com.google.common.truth.Truth.assertThat
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.AppModule
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.data.repositories.FakeHistoryRepositoryImpl
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class ObservePickedUpPlayersHistoryUseCaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(/* testInstance = */ this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeHistoryRepositoryImpl: FakeHistoryRepositoryImpl
    private lateinit var observePickedUpPlayersHistoryUseCase: ObservePickedUpPlayersHistoryUseCase

    @Before
    fun setUp() {
        hiltRule.inject()

        fakeHistoryRepositoryImpl = FakeHistoryRepositoryImpl()
        observePickedUpPlayersHistoryUseCase = ObservePickedUpPlayersHistoryUseCase(
            historyRepository = fakeHistoryRepositoryImpl
        )
    }

    @Test
    fun observePickedPlayersHistoryWithNoPlayers_returnsEmptyPlayersList() = runTest {
        val pickedPlayersHistory = fakeHistoryRepositoryImpl.observePickedUpPlayersHistory()
        assertThat(pickedPlayersHistory.asSnapshot()).isEmpty()
    }

    @Test
    fun observePickedPlayersHistory_returnsEntirePlayersListInDescendingOrder() = runTest {
        fakeHistoryRepositoryImpl.addDummyPlayersToHistory()

        val pickedPlayersHistoryPagingData = fakeHistoryRepositoryImpl
            .observePickedUpPlayersHistory()
        val pickedPlayersHistory = pickedPlayersHistoryPagingData.asSnapshot()

        assertThat(pickedPlayersHistory).isNotEmpty()
        assertThat(pickedPlayersHistory.size).isEqualTo(fakeHistoryRepositoryImpl.playerEntitiesHistory.size)

        for (i in 0..pickedPlayersHistory.size - 2) {
            assertThat(pickedPlayersHistory[i].pickUpTimeInMs)
                .isAtLeast(pickedPlayersHistory[i + 1].pickUpTimeInMs)
        }
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.data.repositories

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.map
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.repositories.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class FakeHistoryRepositoryImpl : HistoryRepository {

    var playerEntitiesHistory = mutableListOf<PlayerEntity>()

    fun addDummyPlayersToHistory() {
        val playersToInsert = ('a'..'z').mapIndexed { index, char ->
            PlayerEntity(
                id = index.toLong(),
                tradeID = index.toString(),
                assetID = index,
                resourceID = index,
                transactionID = index,
                name = char.toString(),
                rating = Random.nextInt(from = 10, until = 99),
                position = "CDM",
                startPrice = 1000,
                buyNowPrice = 2000,
                owners = 1,
                contracts = 1,
                chemistryStyle = "Basic",
                chemistryStyleID = index,
                platform = when (Random.nextBoolean()) {
                    true -> Platform.CONSOLE
                    false -> Platform.PC
                },
                pickUpTimeInSeconds = DateHelper.getCurrentTimeInSeconds().plus(
                    Random.nextLong(
                        from = -600, // 10 minutes ago
                        until = 0 // Now
                    )
                )
            )
        }

        playersToInsert.forEach { playerEntitiesHistory.add(it) }
    }

    override fun observePickedPlayersHistory(): Flow<PagingData<Player>> = flow {
        val sortedPlayerEntitiesHistory = playerEntitiesHistory.toMutableList()
        sortedPlayerEntitiesHistory.sortByDescending { it.pickUpTimeInSeconds }

        val playersPagingData = PagingData.from(
            data = sortedPlayerEntitiesHistory,
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(endOfPaginationReached = true),
                prepend = LoadState.NotLoading(endOfPaginationReached = true),
                append = LoadState.NotLoading(endOfPaginationReached = true)
            )
        ).map { it.toPlayer() }
        emit(playersPagingData)
    }

    override fun observePlayer(playerId: Long): Flow<Player> = flow {
        val player = playerEntitiesHistory.find { playerEntity ->
            playerEntity.id == playerId
        }?.toPlayer()

        player?.let { emit(it) }
    }
}
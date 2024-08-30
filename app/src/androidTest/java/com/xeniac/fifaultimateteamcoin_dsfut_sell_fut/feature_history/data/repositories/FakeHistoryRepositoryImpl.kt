package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.data.repositories

import androidx.paging.PagingData
import androidx.paging.map
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.repositories.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class FakeHistoryRepositoryImpl @Inject constructor() : HistoryRepository {

    var playerEntitiesHistory = mutableListOf<PlayerEntity>()

    fun addDummyPlayersToLatestPlayers() {
        val playersToInsert = mutableListOf<PlayerEntity>()

        ('a'..'z').forEachIndexed { index, char ->
            playersToInsert.add(
                PlayerEntity(
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
            )
        }

        playersToInsert.shuffle()

        playersToInsert.forEach { playerEntitiesHistory.add(it) }
    }

    override fun observePickedPlayersHistory(): Flow<PagingData<Player>> = flow {
        val sortedPlayerEntitiesHistory = playerEntitiesHistory.toMutableList()
        sortedPlayerEntitiesHistory.sortByDescending { it.pickUpTimeInSeconds }

        val playersPagingData = PagingData.from(sortedPlayerEntitiesHistory).map { it.toPlayer() }
        emit(playersPagingData)
    }
}
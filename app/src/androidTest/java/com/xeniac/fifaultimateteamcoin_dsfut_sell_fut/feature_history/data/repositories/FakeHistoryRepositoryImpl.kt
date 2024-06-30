package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.data.repositories

import androidx.paging.PagingData
import androidx.paging.map
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.PlatformDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.repositories.HistoryRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.DateHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class FakeHistoryRepositoryImpl : HistoryRepository {

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
                    platformDto = when (Random.nextBoolean()) {
                        true -> PlatformDto.CONSOLE
                        false -> PlatformDto.PC
                    },
                    pickUpTimeInMillis = DateHelper.getCurrentTimeInMillis().plus(
                        Random.nextLong(
                            from = -600000, // 10 minutes ago
                            until = 0 // Now
                        )
                    ).toString()
                )
            )
        }

        playersToInsert.shuffle()

        playersToInsert.forEach { playerEntitiesHistory.add(it) }
    }

    override fun observePickedPlayersHistory(): Flow<PagingData<Player>> = flow {
        val playersPagingData = PagingData.from(playerEntitiesHistory).map { it.toPlayer() }
        emit(playersPagingData)
    }
}
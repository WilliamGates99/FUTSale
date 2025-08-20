package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mappers.toPlayer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickedUpPlayersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class FakePickedUpPlayersRepositoryImpl : PickedUpPlayersRepository {

    private var latestPlayerEntities = mutableListOf<PlayerEntity>()

    fun addDummyPlayersToLatestPlayers() {
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

        playersToInsert.shuffled().forEach { latestPlayerEntities.add(it) }
    }

    override fun observeLatestPickedUpPlayers(): Flow<List<Player>> = flow {
        val notExpiredLatestPlayerEntities = latestPlayerEntities.filter { playerEntity ->
            val currentTimeInSeconds = DateHelper.getCurrentTimeInSeconds()
            val isNotExpired = currentTimeInSeconds <= playerEntity.expiryTimeInSeconds
            isNotExpired
        }

        val sortedLatestPlayerEntities = notExpiredLatestPlayerEntities.toMutableList()
        sortedLatestPlayerEntities.sortByDescending { it.pickUpTimeInSeconds }

        emit(sortedLatestPlayerEntities.map { it.toPlayer() })
    }

    override fun observePickedUpPlayer(
        playerId: Long
    ): Flow<Player> = flow {
        val player = latestPlayerEntities.find { playerEntity ->
            playerEntity.id == playerId
        }?.toPlayer()

        player?.let { emit(it) }
    }
}
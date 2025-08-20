package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mappers.toPlayer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.DummyPlayersHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickedUpPlayersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class FakePickedUpPlayersRepositoryImpl @Inject constructor() : PickedUpPlayersRepository {

    override fun observeLatestPickedUpPlayers(): Flow<List<Player>> = flow {
        val notExpiredLatestPlayerEntities = DummyPlayersHelper
            .latestPlayerEntities
            .filter { playerEntity ->
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
        val player = DummyPlayersHelper.latestPlayerEntities.find { playerEntity ->
            playerEntity.id == playerId
        }?.toPlayer()

        player?.let { emit(it) }
    }
}
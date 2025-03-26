package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import kotlinx.coroutines.flow.Flow

interface PickedUpPlayersRepository {

    fun observeLatestPickedUpPlayers(): Flow<List<Player>>

    fun observePickedUpPlayer(playerId: Long): Flow<Player>
}
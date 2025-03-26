package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PlayersDao
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickedUpPlayersRepository
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PickedUpPlayersRepositoryImpl @Inject constructor(
    private val playersDao: Lazy<PlayersDao>
) : PickedUpPlayersRepository {

    override fun observeLatestPickedUpPlayers(): Flow<List<Player>> = playersDao.get()
        .observeLatestPickedUpPlayers(
            currentTimeInSeconds = DateHelper.getCurrentTimeInSeconds()
        ).map { playerEntities ->
            playerEntities.map { it.toPlayer() }
        }

    override fun observePickedUpPlayer(
        playerId: Long
    ): Flow<Player> = playersDao.get().observerPickedUpPlayer(
        id = playerId
    ).map { it.toPlayer() }
}
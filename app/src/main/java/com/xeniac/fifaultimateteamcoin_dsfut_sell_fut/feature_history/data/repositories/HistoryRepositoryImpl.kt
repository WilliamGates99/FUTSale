package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.PlayersDao
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.repositories.HistoryRepository
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val playerDao: Lazy<PlayersDao>
) : HistoryRepository {

    override fun observePickedPlayersHistory(): Flow<List<Player>> = playerDao.get()
        .observeAllPlayers().map { playerEntities ->
            playerEntities.map { it.toPlayer() }
        }
}
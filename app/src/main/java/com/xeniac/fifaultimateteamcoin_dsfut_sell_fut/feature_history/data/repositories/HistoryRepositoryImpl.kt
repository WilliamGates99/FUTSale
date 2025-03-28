package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PlayersDao
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.repositories.HistoryRepository
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val pager: Pager<Int, PlayerEntity>,
    private val playersDao: Lazy<PlayersDao>
) : HistoryRepository {

    override fun observePickedUpPlayersHistory(): Flow<PagingData<Player>> =
        pager.flow.map { pagingData ->
            pagingData.map { it.toPlayer() }
        }

    override fun observerPickedUpPlayer(
        playerId: Long
    ): Flow<Player> = playersDao.get().observerPickedUpPlayer(
        id = playerId
    ).map { it.toPlayer() }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.repositories.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val pager: Pager<Int, PlayerEntity>
) : HistoryRepository {

    override fun observePickedPlayersHistory(): Flow<PagingData<Player>> =
        pager.flow.map { pagingData ->
            pagingData.map { it.toPlayer() }
        }
}
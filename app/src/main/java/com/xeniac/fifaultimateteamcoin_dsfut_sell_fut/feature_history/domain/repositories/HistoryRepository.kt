package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_history.domain.repositories

import androidx.paging.PagingData
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun observePickedUpPlayersHistory(): Flow<PagingData<Player>>

    fun observerPickedUpPlayer(playerId: Long): Flow<Player>
}
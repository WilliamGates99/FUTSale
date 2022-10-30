package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.DsfutResponse
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.DsfutRepository
import retrofit2.Response

class FakeDsfutRepositoryImp : DsfutRepository {

    private val players = mutableListOf<Player>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    fun addPlayer(player: Player) {
        players.add(player)
    }

    override suspend fun pickUpPlayer(
        platform: String,
        partnerId: String,
        timestamp: String,
        signature: String,
        minPrice: Int?,
        maxPrice: Int?,
        takeAfter: Int?,
        fifaVersion: Int
    ): Response<DsfutResponse> {
        TODO("Not yet implemented")
    }
}
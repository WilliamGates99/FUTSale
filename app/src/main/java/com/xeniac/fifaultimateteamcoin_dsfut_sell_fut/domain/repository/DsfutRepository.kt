package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository

import androidx.lifecycle.LiveData
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local.models.PickedUpPlayer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Resource

interface DsfutRepository {

    suspend fun insertPickedUpPlayer(pickedUpPlayer: PickedUpPlayer)

    suspend fun deletePickedUpPlayer(pickedUpPlayer: PickedUpPlayer)

    fun observeAllPickedUpPlayers(): LiveData<List<PickedUpPlayer>>

    suspend fun pickUpPlayer(
        platform: String,
        partnerId: String,
        secretKey: String,
        minPrice: Int? = null,
        maxPrice: Int? = null,
        takeAfter: Int? = null,
        fifaVersion: Int = 24
    ): Resource<Player>
}
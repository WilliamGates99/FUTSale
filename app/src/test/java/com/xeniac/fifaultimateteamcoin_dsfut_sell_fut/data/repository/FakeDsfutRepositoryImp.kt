package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local.models.PickedUpPlayer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.DsfutResponse
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.DsfutRepository
import retrofit2.Response

class FakeDsfutRepositoryImp : DsfutRepository {

    private val players = mutableListOf<Player>()

    private val pickedUpPlayers = mutableListOf<PickedUpPlayer>()
    private val observablePickedUpPlayers = MutableLiveData<List<PickedUpPlayer>>(pickedUpPlayers)

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    fun addPlayer(player: Player) {
        players.add(player)
    }

    private fun refreshLiveData() {
        observablePickedUpPlayers.postValue(pickedUpPlayers)
    }

    override suspend fun insertPickedUpPlayer(pickedUpPlayer: PickedUpPlayer) {
        pickedUpPlayers.add(pickedUpPlayer)
        refreshLiveData()
    }

    override suspend fun deletePickedUpPlayer(pickedUpPlayer: PickedUpPlayer) {
        pickedUpPlayers.remove(pickedUpPlayer)
        refreshLiveData()
    }

    override fun observeAllPickedUpPlayers(): LiveData<List<PickedUpPlayer>> =
        observablePickedUpPlayers

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
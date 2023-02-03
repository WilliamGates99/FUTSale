package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local.models.PickedUpPlayer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.DsfutRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Resource
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.UiText

class FakeDsfutRepositoryImp : DsfutRepository {

    private val pickedUpPlayers = mutableListOf<PickedUpPlayer>()
    private val observablePickedUpPlayers = MutableLiveData<List<PickedUpPlayer>>(pickedUpPlayers)

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
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
        secretKey: String,
        minPrice: Int?,
        maxPrice: Int?,
        takeAfter: Int?,
        fifaVersion: Int
    ): Resource<Player> = if (shouldReturnNetworkError) {
        Resource.Error(UiText.DynamicString("No internet connection"))
    } else {
        val testPlayer = Player(
            assetID = 1,
            buyNowPrice = 100,
            expires = 100,
            name = "Test Player",
            position = "CDM",
            rating = 89,
            resourceID = 1,
            startPrice = 50,
            tradeID = 100L,
            transactionID = 10
        )
        Resource.Success(testPlayer)
    }
}
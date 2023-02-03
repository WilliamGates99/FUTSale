package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local.models.PickedUpPlayer

@Dao
interface DsfutDao {

    @Upsert
    suspend fun insertPickedUpPlayer(pickedUpPlayer: PickedUpPlayer)

    @Delete
    suspend fun deletePickedUpPlayer(pickedUpPlayer: PickedUpPlayer)

    @Query("SELECT * FROM picked_up_players ORDER BY pickUpTimeInMillis DESC")
    fun observeAllPickedUpPlayers(): LiveData<List<PickedUpPlayer>>
}
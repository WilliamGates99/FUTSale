package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.entities.PickedUpPlayerEntity

@Dao
interface PlayersDao {

    @Upsert
    suspend fun insertPickedUpPlayer(pickedUpPlayer: PickedUpPlayerEntity)

    @Delete
    suspend fun deletePickedUpPlayer(pickedUpPlayer: PickedUpPlayerEntity)

    @Query("SELECT * FROM picked_up_players ORDER BY pickUpTimeInMillis DESC")
    fun observeAllPickedUpPlayers(): LiveData<List<PickedUpPlayerEntity>>
}
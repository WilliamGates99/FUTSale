package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.entities.PlayerEntity

@Dao
interface PlayersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(playerEntity: PlayerEntity)

    @Query("DELETE FROM players")
    suspend fun clearPlayers()

    @Delete
    suspend fun deletePlayer(playerEntity: PlayerEntity)

    @Query("SELECT * FROM players ORDER BY pickUpTimeInMillis DESC")
    suspend fun getPlayers(): List<PlayerEntity>

    @Query("SELECT * FROM players WHERE id = :id")
    suspend fun getPlayer(id: Int): PlayerEntity
}
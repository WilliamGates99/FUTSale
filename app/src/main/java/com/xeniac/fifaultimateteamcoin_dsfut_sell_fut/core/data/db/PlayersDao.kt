package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.entities.PlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(playerEntity: PlayerEntity)

    @Query("DELETE FROM players")
    suspend fun clearPlayers()

    @Delete
    suspend fun deletePlayer(playerEntity: PlayerEntity)

    @Query("SELECT * FROM players ORDER BY pickUpTimeInMillis DESC LIMIT 3")
    fun observeThreeLatestPlayers(): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players ORDER BY pickUpTimeInMillis DESC")
    fun observeAllPlayers(): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players WHERE id = :id")
    fun getPlayer(id: Int): Flow<PlayerEntity>
}
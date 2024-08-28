package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(playerEntity: PlayerEntity)

    @Query("DELETE FROM players")
    suspend fun clearPlayers()

    @Delete
    suspend fun deletePlayer(playerEntity: PlayerEntity)

    @Query("SELECT * FROM players")
    fun getPlayers(): List<PlayerEntity>

    @Query(
        """
        SELECT * FROM players
        WHERE :currentTimeInSeconds <= expiry_time_in_seconds
        ORDER BY pick_up_time_in_seconds DESC
        """
    )
    fun observeLatestPickedPlayers(currentTimeInSeconds: Long): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players ORDER BY pick_up_time_in_seconds DESC")
    fun pagingSource(): PagingSource<Int, PlayerEntity>

    @Query("SELECT * FROM players WHERE id = :id")
    fun getPlayer(id: Long): Flow<PlayerEntity>
}
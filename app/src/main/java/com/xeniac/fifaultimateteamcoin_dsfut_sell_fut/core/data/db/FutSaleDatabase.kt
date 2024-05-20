package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.entities.PickedUpPlayerEntity

@Database(
    entities = [PickedUpPlayerEntity::class],
    version = 1
)
abstract class FutSaleDatabase : RoomDatabase() {
    abstract fun playersDao(): PlayersDao
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local.models.PickedUpPlayer

@Database(
    entities = [PickedUpPlayer::class],
    version = 1
)
abstract class FutSaleDatabase : RoomDatabase() {

    abstract fun playersDao(): PlayersDao
}
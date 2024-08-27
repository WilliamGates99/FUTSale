package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.migrations.Migration1To2

@Database(
    entities = [PlayerEntity::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = Migration1To2::class)
    ]
)
abstract class FutSaleDatabase : RoomDatabase() {
    abstract fun playersDao(): PlayersDao
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_TO_4 = object : Migration(startVersion = 3, endVersion = 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create a new temporary table with the updated schema
        db.execSQL(
            sql = """
                CREATE TABLE players_temp (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                trade_id TEXT NOT NULL,
                asset_id INTEGER NOT NULL,
                resource_id INTEGER NOT NULL,
                transaction_id INTEGER NOT NULL,
                name TEXT NOT NULL,
                rating INTEGER NOT NULL,
                position TEXT NOT NULL,
                start_price INTEGER NOT NULL,
                buy_now_price INTEGER NOT NULL,
                owners INTEGER NOT NULL,
                contracts INTEGER NOT NULL,
                chemistry_style TEXT NOT NULL,
                chemistry_style_id INTEGER NOT NULL,
                platform TEXT NOT NULL,
                pick_up_time_in_seconds INTEGER NOT NULL,
                expiry_time_in_seconds INTEGER NOT NULL
                )
                """.trimIndent()
        )

        // Copy the data from the old table to the new table
        db.execSQL(
            sql = """
                INSERT INTO players_temp (
                id, trade_id, asset_id, resource_id, transaction_id, name, rating, position,
                start_price, buy_now_price, owners, contracts, chemistry_style, chemistry_style_id,
                platform, pick_up_time_in_seconds, expiry_time_in_seconds
                )
                SELECT id, trade_id, asset_id, resource_id, transaction_id, name, rating, position,
                start_price, buy_now_price, owners, contracts, chemistry_style, chemistry_style_id,
                platform, pick_up_time_in_seconds,
                pick_up_time_in_seconds + 180 -- Add 3 minutes in seconds
                FROM players
                """.trimIndent()
        )

        // Remove the old table
        db.execSQL(sql = "DROP TABLE players")

        // Rename the new table to the old table's name
        db.execSQL(sql = "ALTER TABLE players_temp RENAME TO players")
    }
}
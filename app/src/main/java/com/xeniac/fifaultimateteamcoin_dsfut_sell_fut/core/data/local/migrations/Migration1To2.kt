package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.migrations

import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

@RenameColumn(
    tableName = "players",
    fromColumnName = "tradeID",
    toColumnName = "trade_id"
)
@RenameColumn(
    tableName = "players",
    fromColumnName = "assetID",
    toColumnName = "asset_id"
)
@RenameColumn(
    tableName = "players",
    fromColumnName = "resourceID",
    toColumnName = "resource_id"
)
@RenameColumn(
    tableName = "players",
    fromColumnName = "transactionID",
    toColumnName = "transaction_id"
)
@RenameColumn(
    tableName = "players",
    fromColumnName = "startPrice",
    toColumnName = "start_price"
)
@RenameColumn(
    tableName = "players",
    fromColumnName = "buyNowPrice",
    toColumnName = "buy_now_price"
)
@RenameColumn(
    tableName = "players",
    fromColumnName = "chemistryStyle",
    toColumnName = "chemistry_style"
)
@RenameColumn(
    tableName = "players",
    fromColumnName = "chemistryStyleID",
    toColumnName = "chemistry_style_id"
)
@RenameColumn(
    tableName = "players",
    fromColumnName = "pickUpTimeInMillis",
    toColumnName = "pick_up_time_in_seconds"
)
class Migration1To2 : AutoMigrationSpec
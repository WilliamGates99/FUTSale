package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper

@Entity(tableName = "players")
data class PlayerEntity(
    @ColumnInfo(name = "trade_id")
    val tradeID: String,
    @ColumnInfo(name = "asset_id")
    val assetID: Int,
    @ColumnInfo(name = "resource_id")
    val resourceID: Int,
    @ColumnInfo(name = "transaction_id")
    val transactionID: Int,
    val name: String,
    val rating: Int,
    val position: String,
    @ColumnInfo(name = "start_price")
    val startPrice: Int,
    @ColumnInfo(name = "buy_now_price")
    val buyNowPrice: Int,
    val owners: Int,
    val contracts: Int,
    @ColumnInfo(name = "chemistry_style")
    val chemistryStyle: String,
    @ColumnInfo(name = "chemistry_style_id")
    val chemistryStyleID: Int,
    @ColumnInfo(name = "platform")
    val platform: Platform,
    @ColumnInfo(name = "pick_up_time_in_seconds")
    val pickUpTimeInSeconds: Long = DateHelper.getCurrentTimeInSeconds(),
    @ColumnInfo(name = "expiry_time_in_seconds")
    val expiryTimeInSeconds: Long = pickUpTimeInSeconds + Constants.PLAYER_EXPIRY_TIME_IN_SECONDS,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
)
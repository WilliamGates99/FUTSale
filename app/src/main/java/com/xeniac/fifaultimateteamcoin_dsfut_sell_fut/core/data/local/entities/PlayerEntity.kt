package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.formatToString
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

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
) {
    fun toPlayer(): Player {
        return Player(
            tradeID = tradeID.toLong(),
            assetID = assetID,
            resourceID = resourceID,
            transactionID = transactionID,
            name = name,
            rating = DecimalFormat(
                /* pattern = */ "00",
                /* symbols = */ DecimalFormatSymbols(Locale.US)
            ).format(rating),
            position = position,
            startPrice = startPrice.formatToString(),
            buyNowPrice = buyNowPrice.formatToString(),
            owners = owners.toString(),
            contracts = contracts.toString(),
            chemistryStyle = chemistryStyle,
            chemistryStyleID = chemistryStyleID,
            platform = platform,
            pickUpTimeInMs = pickUpTimeInSeconds * 1000,
            expiryTimeInMs = expiryTimeInSeconds * 1000,
            id = id
        )
    }
}
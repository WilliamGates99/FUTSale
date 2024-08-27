package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.dto.PlatformDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.formatNumber
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
    val platformDto: PlatformDto,
    @ColumnInfo(name = "pick_up_time_in_seconds")
    val pickUpTimeInSeconds: Long = DateHelper.getCurrentTimeInSeconds(),
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
            startPrice = formatNumber(startPrice),
            buyNowPrice = formatNumber(buyNowPrice),
            owners = owners,
            contracts = contracts,
            chemistryStyle = chemistryStyle,
            chemistryStyleID = chemistryStyleID,
            platform = platformDto.toPlatform(),
            pickUpTimeInMs = pickUpTimeInSeconds * 1000,
            expiryTimeInMs = (pickUpTimeInSeconds + Constants.PLAYER_EXPIRY_TIME_IN_SECONDS) * 1000,
            id = id
        )
    }
}
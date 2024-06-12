package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.formatNumber
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.Constants
import okhttp3.internal.toLongOrDefault

@Entity(tableName = "players")
data class PlayerEntity(
    val tradeID: String,
    val assetID: Int,
    val resourceID: Int,
    val transactionID: Int,
    val name: String,
    val rating: Int,
    val position: String,
    val startPrice: Int,
    val buyNowPrice: Int,
    val owners: Int,
    val contracts: Int,
    val chemistryStyle: String,
    val chemistryStyleID: Int,
    val pickUpTimeInMillis: String = DateHelper.getCurrentTimeInMillis().toString(),
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
) {
    fun toPlayer(): Player {
        val pickUpTimeInMillis = pickUpTimeInMillis.toLongOrDefault(defaultValue = 0)

        return Player(
            tradeID = tradeID.toLong(),
            assetID = assetID,
            resourceID = resourceID,
            transactionID = transactionID,
            name = name,
            rating = rating,
            position = position,
            startPrice = formatNumber(startPrice),
            buyNowPrice = formatNumber(buyNowPrice),
            owners = owners,
            contracts = contracts,
            chemistryStyle = chemistryStyle,
            chemistryStyleID = chemistryStyleID,
            pickUpTimeInMillis = pickUpTimeInMillis,
            expiryTimeInMillis = pickUpTimeInMillis + Constants.PLAYER_EXPIRY_TIME_IN_MS,
            id = id
        )
    }
}
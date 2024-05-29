package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.formatNumber

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
    fun toPlayer(): Player = Player(
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
        id = id
    )
}
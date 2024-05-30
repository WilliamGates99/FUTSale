package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.dto.PlayerDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    val tradeID: Long,
    val assetID: Int,
    val resourceID: Int,
    val transactionID: Int,
    val name: String,
    val rating: Int,
    val position: String,
    val startPrice: String,
    val buyNowPrice: String,
    val owners: Int,
    val contracts: Int,
    val chemistryStyle: String,
    val chemistryStyleID: Int,
    val id: Int? = null
) : Parcelable {
    fun toPlayerDto(): PlayerDto = PlayerDto(
        tradeID = tradeID,
        assetID = assetID,
        resourceID = resourceID,
        transactionID = transactionID,
        name = name,
        rating = rating,
        position = position,
        startPrice = startPrice.toInt(),
        buyNowPrice = buyNowPrice.toInt(),
        owners = owners,
        contracts = contracts,
        chemistryStyle = chemistryStyle,
        chemistryStyleID = chemistryStyleID,
        expires = 0
    )

    fun toPlayerEntity(): PlayerEntity = PlayerEntity(
        tradeID = tradeID.toString(),
        assetID = assetID,
        resourceID = resourceID,
        transactionID = transactionID,
        name = name,
        rating = rating,
        position = position,
        startPrice = startPrice.toInt(),
        buyNowPrice = buyNowPrice.toInt(),
        owners = owners,
        contracts = contracts,
        chemistryStyle = chemistryStyle,
        chemistryStyleID = chemistryStyleID,
        id = id
    )
}
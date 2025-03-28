package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models

import android.os.Parcelable
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.dto.PlayerDto
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Player(
    val tradeID: Long,
    val assetID: Int,
    val resourceID: Int,
    val transactionID: Int,
    val name: String,
    val rating: String,
    val position: String,
    val startPrice: String,
    val buyNowPrice: String,
    val owners: String,
    val contracts: String,
    val chemistryStyle: String,
    val chemistryStyleID: Int,
    val platform: Platform,
    val pickUpTimeInMs: Long = 0,
    val expiryTimeInMs: Long = 0,
    val id: Long? = null
) : Parcelable {
    fun toPlayerDto(): PlayerDto = PlayerDto(
        tradeID = tradeID,
        assetID = assetID,
        resourceID = resourceID,
        transactionID = transactionID,
        name = name,
        rating = rating.toInt(),
        position = position,
        startPrice = startPrice.toInt(),
        buyNowPrice = buyNowPrice.toInt(),
        owners = owners.toInt(),
        contracts = contracts.toInt(),
        chemistryStyle = chemistryStyle,
        chemistryStyleID = chemistryStyleID,
        expires = 0,
        platform = platform
    )

    fun toPlayerEntity(): PlayerEntity = PlayerEntity(
        tradeID = tradeID.toString(),
        assetID = assetID,
        resourceID = resourceID,
        transactionID = transactionID,
        name = name,
        rating = rating.toInt(),
        position = position,
        startPrice = startPrice.toInt(),
        buyNowPrice = buyNowPrice.toInt(),
        owners = owners.toInt(),
        contracts = contracts.toInt(),
        chemistryStyle = chemistryStyle,
        chemistryStyleID = chemistryStyleID,
        platform = platform,
        pickUpTimeInSeconds = pickUpTimeInMs / 1000,
        expiryTimeInSeconds = expiryTimeInMs / 1000,
        id = id
    )
}
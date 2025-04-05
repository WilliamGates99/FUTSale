package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mappers

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.dto.PlayerDto

fun Player.toPlayerDto(): PlayerDto = PlayerDto(
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

fun Player.toPlayerEntity(): PlayerEntity = PlayerEntity(
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
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mappers

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.formatToString
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.remote.PlayerDto
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun PlayerEntity.toPlayer(): Player {
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
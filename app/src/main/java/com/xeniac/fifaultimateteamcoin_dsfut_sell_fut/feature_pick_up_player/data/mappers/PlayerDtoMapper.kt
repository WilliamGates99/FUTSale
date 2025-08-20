package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.mappers

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.formatToString
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.dto.PlayerDto
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun PlayerDto.toPlayerEntity(): PlayerEntity = PlayerEntity(
    tradeID = tradeID.toString(),
    assetID = assetID,
    resourceID = resourceID,
    transactionID = transactionID,
    name = name,
    rating = rating,
    position = position,
    startPrice = startPrice,
    buyNowPrice = buyNowPrice,
    owners = owners,
    contracts = contracts,
    chemistryStyle = chemistryStyle,
    chemistryStyleID = chemistryStyleID,
    platform = platform
)

fun PlayerDto.toPlayer(): Player = Player(
    tradeID = tradeID,
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
    platform = platform
)
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.dto

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.presentation.utils.formatNumber
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Serializable
data class PlayerDto(
    @SerialName("tradeID")
    val tradeID: Long,
    @SerialName("assetID")
    val assetID: Int,
    @SerialName("resourceID")
    val resourceID: Int,
    @SerialName("transactionID")
    val transactionID: Int,
    @SerialName("name")
    val name: String,
    @SerialName("rating")
    val rating: Int,
    @SerialName("position")
    val position: String,
    @SerialName("startPrice")
    val startPrice: Int,
    @SerialName("buyNowPrice")
    val buyNowPrice: Int,
    @SerialName("owners")
    val owners: Int,
    @SerialName("contracts")
    val contracts: Int,
    @SerialName("chemistryStyle")
    val chemistryStyle: String,
    @SerialName("chemistryStyleID")
    val chemistryStyleID: Int,
    @SerialName("expires")
    val expires: Int,
    val platform: Platform = Platform.CONSOLE
) {
    fun toPlayerEntity(): PlayerEntity = PlayerEntity(
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

    fun toPlayer(): Player = Player(
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
        startPrice = formatNumber(startPrice),
        buyNowPrice = formatNumber(buyNowPrice),
        owners = owners,
        contracts = contracts,
        chemistryStyle = chemistryStyle,
        chemistryStyleID = chemistryStyleID,
        platform = platform
    )
}
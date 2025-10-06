package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.remote

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
)
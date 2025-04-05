package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PickUpPlayerResponseDto(
    @SerialName("error")
    val error: String,
    @SerialName("message")
    val message: String,
    @SerialName("player")
    val playerDto: PlayerDto? = null
)
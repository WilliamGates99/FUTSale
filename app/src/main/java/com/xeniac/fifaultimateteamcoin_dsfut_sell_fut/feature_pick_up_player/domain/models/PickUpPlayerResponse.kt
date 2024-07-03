package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.models

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.dto.PickUpPlayerResponseDto

data class PickUpPlayerResponse(
    val error: String,
    val message: String,
    val player: Player?
) {
    fun toPickUpPlayerResponseDto(): PickUpPlayerResponseDto = PickUpPlayerResponseDto(
        error = error,
        message = message,
        playerDto = player?.toPlayerDto()
    )
}
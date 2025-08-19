package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.errors.PickUpPlayerError

interface PickUpPlayerRepository {

    suspend fun pickUpPlayer(
        partnerId: String,
        secretKey: String,
        minPrice: String?,
        maxPrice: String?,
        takeAfterDelayInSeconds: Int?
    ): Result<Player, PickUpPlayerError>

    sealed class EndPoints(val url: String) {
        data class PickUpPlayer(
            val platform: String,
            val partnerId: String,
            val timestamp: Long,
            val signature: String,
            val gameYear: Int = 25
        ) : EndPoints(
            url = "${BuildConfig.DSFUT_HTTP_BASE_URL}/$gameYear/$platform/$partnerId/$timestamp/$signature"
        )
    }
}
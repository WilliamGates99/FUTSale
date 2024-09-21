package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.BuildConfig
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError
import kotlinx.coroutines.flow.Flow

typealias TimerValueInSeconds = Int

const val FIFA_VERSION = 24

interface PickUpPlayerRepository {

    fun observeLatestPickedPlayers(): Flow<List<Player>>

    fun observeCountDownTimer(expiryTimeInMs: Long): Flow<TimerValueInSeconds>

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
            val gameYear: Int = FIFA_VERSION
        ) : EndPoints(
            url = "${BuildConfig.DSFUT_HTTP_BASE_URL}/$gameYear/$platform/$partnerId/$timestamp/$signature"
        )
    }
}
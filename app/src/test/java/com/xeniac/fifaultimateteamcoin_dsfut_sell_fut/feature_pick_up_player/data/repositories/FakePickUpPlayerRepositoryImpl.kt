package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.PlatformDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.TimerValueInSeconds
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class FakePickUpPlayerRepositoryImpl : PickUpPlayerRepository {

    private var latestPlayerEntities = mutableListOf<PlayerEntity>()

    fun addDummyPlayersToLatestPlayers() {
        val playersToInsert = mutableListOf<PlayerEntity>()

        ('a'..'z').forEachIndexed { index, char ->
            playersToInsert.add(
                PlayerEntity(
                    tradeID = index.toString(),
                    assetID = index,
                    resourceID = index,
                    transactionID = index,
                    name = char.toString(),
                    rating = Random.nextInt(from = 10, until = 99),
                    position = "CDM",
                    startPrice = 1000,
                    buyNowPrice = 2000,
                    owners = 1,
                    contracts = 1,
                    chemistryStyle = "Basic",
                    chemistryStyleID = index,
                    platformDto = when (Random.nextBoolean()) {
                        true -> PlatformDto.CONSOLE
                        false -> PlatformDto.PC
                    },
                    pickUpTimeInMillis = DateHelper.getCurrentTimeInMillis().plus(
                        Random.nextLong(
                            from = -600000, // 10 minutes ago
                            until = 0 // Now
                        )
                    ).toString()
                )
            )
        }

        playersToInsert.shuffle()

        playersToInsert.forEach { latestPlayerEntities.add(it) }
    }

    override fun observeLatestPickedPlayers(): Flow<List<Player>> = flow {
        latestPlayerEntities.sortByDescending { it.pickUpTimeInMillis }
        emit(latestPlayerEntities.map { it.toPlayer() })
    }

    override fun observeCountDownTimer(expiryTimeInMs: Long): Flow<TimerValueInSeconds> = flow {
        val currentTime = DateHelper.getCurrentTimeInMillis()
        val expiryTime = currentTime + expiryTimeInMs
        val isPlayerExpired = DateHelper.isPickedPlayerExpired(expiryTime)

        if (isPlayerExpired) {
            emit(0)
        } else {
            val timerStartTimeInMs = expiryTime - currentTime
            var currentTimerValueInMs = timerStartTimeInMs

            while (currentTimerValueInMs >= 0) {
                emit((currentTimerValueInMs / 1000).toInt())
                delay(timeMillis = Constants.COUNT_DOWN_TIMER_INTERVAL_IN_MS)
                currentTimerValueInMs -= Constants.COUNT_DOWN_TIMER_INTERVAL_IN_MS
            }
        }
    }

    override suspend fun pickUpPlayer(
        partnerId: String,
        secretKey: String,
        minPrice: String?,
        maxPrice: String?,
        takeAfterDelayInSeconds: Int?
    ): Result<Player, PickUpPlayerError> {
        TODO("Not yet implemented")
    }
}
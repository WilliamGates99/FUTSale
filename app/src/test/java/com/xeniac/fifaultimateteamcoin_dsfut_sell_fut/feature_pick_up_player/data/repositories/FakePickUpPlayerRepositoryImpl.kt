package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.dto.PlatformDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.dto.PickUpPlayerResponseDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.dto.PlayerDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.HashHelper.getMd5Signature
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.TimerValueInSeconds
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random

class FakePickUpPlayerRepositoryImpl : PickUpPlayerRepository {

    private var latestPlayerEntities = mutableListOf<PlayerEntity>()
    private var pickUpPlayerHttpStatusCode = HttpStatusCode.OK
    private var isPlayersQueueEmpty = false

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

    fun setPickUpPlayerHttpStatusCode(httpStatusCode: HttpStatusCode) {
        pickUpPlayerHttpStatusCode = httpStatusCode
    }

    fun setIsPlayersQueueEmpty(isEmpty: Boolean) {
        isPlayersQueueEmpty = isEmpty
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
        val mockEngine = MockEngine {
            val pickUpPlayerResponseDto = if (pickUpPlayerHttpStatusCode == HttpStatusCode.OK) {
                if (isPlayersQueueEmpty) {
                    PickUpPlayerResponseDto(
                        error = "empty",
                        message = "Queue is empty"
                    )
                } else {
                    PickUpPlayerResponseDto(
                        error = "",
                        message = "1 player popped",
                        playerDto = PlayerDto(
                            tradeID = 1,
                            assetID = 1,
                            resourceID = 1,
                            transactionID = 1,
                            name = "Test Player",
                            rating = 88,
                            position = "GK",
                            startPrice = 10000,
                            buyNowPrice = 15000,
                            owners = 1,
                            contracts = 1,
                            chemistryStyle = "Basic",
                            chemistryStyleID = 1,
                            expires = 0
                        )
                    )
                }
            } else {
                PickUpPlayerResponseDto(
                    error = "empty",
                    message = "Queue is empty"
                )
            }

            respond(
                content = Json.encodeToString(pickUpPlayerResponseDto),
                status = pickUpPlayerHttpStatusCode,
                headers = headersOf(
                    name = HttpHeaders.ContentType,
                    value = "application/json"
                )
            )
        }

        val testClient = HttpClient(engine = mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    coerceInputValues = true
                })
            }
        }

        val timestamp = DateHelper.getCurrentTimeInMillis()
        val signature = getMd5Signature(partnerId, secretKey, timestamp)

        val response = testClient.get(
            urlString = PickUpPlayerRepository.EndPoints.PickUpPlayer(
                platform = PlatformDto.CONSOLE.value,
                partnerId = partnerId,
                timestamp = timestamp,
                signature = signature
            ).url
        ) {
            parameter(key = "min_buy", value = minPrice)
            parameter(key = "max_buy", value = maxPrice)
            parameter(key = "take_after", value = takeAfterDelayInSeconds)
        }

        return when (response.status.value) {
            HttpStatusCode.OK.value -> { // Code: 200
                val pickUpPlayerResponseDto = response.body<PickUpPlayerResponseDto>()
                val playerDto = pickUpPlayerResponseDto.playerDto

                val isPlayerPickedUpSuccessfully = playerDto != null
                if (isPlayerPickedUpSuccessfully) {
                    val playerEntity = playerDto!!.toPlayerEntity()
                    Result.Success(playerEntity.toPlayer())
                } else {
                    val pickUpPlayerError = when (pickUpPlayerResponseDto.error) {
                        Constants.ERROR_DSFUT_BLOCK -> PickUpPlayerError.Network.DsfutBlock(message = pickUpPlayerResponseDto.message)
                        Constants.ERROR_DSFUT_EMPTY -> PickUpPlayerError.Network.DsfutEmpty
                        Constants.ERROR_DSFUT_LIMIT -> PickUpPlayerError.Network.DsfutLimit
                        Constants.ERROR_DSFUT_MAINTENANCE -> PickUpPlayerError.Network.DsfutMaintenance
                        Constants.ERROR_DSFUT_PARAMETERS -> PickUpPlayerError.Network.DsfutParameters
                        Constants.ERROR_DSFUT_SIGNATURE -> PickUpPlayerError.Network.DsfutSignature
                        Constants.ERROR_DSFUT_THROTTLE -> PickUpPlayerError.Network.DsfutThrottle
                        Constants.ERROR_DSFUT_UNIX_TIME -> PickUpPlayerError.Network.DsfutUnitTime
                        else -> PickUpPlayerError.Network.SomethingWentWrong
                    }

                    Result.Error(pickUpPlayerError)
                }
            }
            else -> Result.Error(PickUpPlayerError.Network.SomethingWentWrong)
        }
    }
}
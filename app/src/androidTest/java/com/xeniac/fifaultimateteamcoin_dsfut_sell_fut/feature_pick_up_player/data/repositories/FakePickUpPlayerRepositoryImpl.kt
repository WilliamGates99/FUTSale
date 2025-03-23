package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.dto.PickUpPlayerResponseDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.dto.PlayerDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.DateHelper.isPickedPlayerExpired
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.HashHelper.getMd5Signature
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.TimerValueInSeconds
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.random.Random

class FakePickUpPlayerRepositoryImpl @Inject constructor() : PickUpPlayerRepository {

    companion object {
        val dummyPlayerDto = PlayerDto(
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
    }

    private var isNetworkAvailable = true
    private var pickUpPlayerHttpStatusCode = HttpStatusCode.OK
    private var isPlayersQueueEmpty = false

    private var pickedUpPlayerId = 0L
    private var latestPlayerEntities = SnapshotStateList<PlayerEntity>()

    fun isNetworkAvailable(isAvailable: Boolean) {
        isNetworkAvailable = isAvailable
    }

    fun setPickUpPlayerHttpStatusCode(httpStatusCode: HttpStatusCode) {
        pickUpPlayerHttpStatusCode = httpStatusCode
    }

    fun setIsPlayersQueueEmpty(isEmpty: Boolean) {
        isPlayersQueueEmpty = isEmpty
    }

    fun addDummyPlayersToLatestPlayers() {
        val playersToInsert = ('a'..'z').mapIndexed { index, char ->
            PlayerEntity(
                id = index.toLong(),
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
                platform = when (Random.nextBoolean()) {
                    true -> Platform.CONSOLE
                    false -> Platform.PC
                },
                pickUpTimeInSeconds = DateHelper.getCurrentTimeInSeconds().plus(
                    Random.nextLong(
                        from = -600, // 10 minutes ago
                        until = 0 // Now
                    )
                )
            )
        }

        playersToInsert.shuffled().forEach { latestPlayerEntities.add(it) }
    }

    override fun observeLatestPickedPlayers(): Flow<List<Player>> = snapshotFlow {
        val notExpiredLatestPlayerEntities = latestPlayerEntities.filter { playerEntity ->
            val currentTimeInSeconds = DateHelper.getCurrentTimeInSeconds()
            val isNotExpired = currentTimeInSeconds <= playerEntity.expiryTimeInSeconds
            isNotExpired
        }

        val sortedLatestPlayerEntities = notExpiredLatestPlayerEntities.toMutableList()
        sortedLatestPlayerEntities.sortByDescending { it.pickUpTimeInSeconds }

        sortedLatestPlayerEntities.map { it.toPlayer() }
    }

    override fun observePickedUpPlayer(playerId: Long): Flow<Player> = flow {
        val player = latestPlayerEntities.find { playerEntity ->
            playerEntity.id == playerId
        }?.toPlayer()

        player?.let { emit(it) }
    }

    override fun observeCountDownTimer(expiryTimeInMs: Long): Flow<TimerValueInSeconds> = flow {
        val currentTime = DateHelper.getCurrentTimeInMillis()
        val expiryTime = currentTime + expiryTimeInMs
        val isPlayerExpired = isPickedPlayerExpired(expiryTime)

        if (isPlayerExpired) {
            emit(0)
            return@flow
        }

        val timerStartTimeInMs = expiryTime - currentTime
        var currentTimerValueInMs = timerStartTimeInMs

        while (currentTimerValueInMs >= 0) {
            emit((currentTimerValueInMs / 1000).toInt())
            delay(timeMillis = Constants.COUNT_DOWN_TIMER_INTERVAL_IN_MS)
            currentTimerValueInMs -= Constants.COUNT_DOWN_TIMER_INTERVAL_IN_MS
        }
    }

    override suspend fun pickUpPlayer(
        partnerId: String,
        secretKey: String,
        minPrice: String?,
        maxPrice: String?,
        takeAfterDelayInSeconds: Int?
    ): Result<Player, PickUpPlayerError> {
        if (!isNetworkAvailable) {
            return Result.Error(PickUpPlayerError.Network.Offline)
        }

        val mockEngine = MockEngine {
            val pickUpPlayerResponseDto = if (pickUpPlayerHttpStatusCode == HttpStatusCode.OK) {
                if (isPlayersQueueEmpty) {
                    PickUpPlayerResponseDto(
                        error = Constants.ERROR_DSFUT_EMPTY,
                        message = "Queue is empty"
                    )
                } else {
                    PickUpPlayerResponseDto(
                        error = "",
                        message = "1 player popped",
                        playerDto = dummyPlayerDto
                    )
                }
            } else {
                PickUpPlayerResponseDto(
                    error = Constants.ERROR_DSFUT_EMPTY,
                    message = "Queue is empty"
                )
            }

            respond(
                content = Json.encodeToString(pickUpPlayerResponseDto),
                status = pickUpPlayerHttpStatusCode,
                headers = headersOf(
                    name = HttpHeaders.ContentType,
                    value = ContentType.Application.Json.toString()
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
            install(DefaultRequest) {
                contentType(ContentType.Application.Json)
            }
        }

        val timestampInSeconds = DateHelper.getCurrentTimeInSeconds()
        val signature = getMd5Signature(
            partnerId = partnerId,
            secretKey = secretKey,
            timestamp = timestampInSeconds
        )

        val response = testClient.get(
            urlString = PickUpPlayerRepository.EndPoints.PickUpPlayer(
                platform = Platform.CONSOLE.value,
                partnerId = partnerId,
                timestamp = timestampInSeconds,
                signature = signature
            ).url
        ) {
            parameter(key = "min_buy", value = minPrice)
            parameter(key = "max_buy", value = maxPrice)
            parameter(key = "take_after", value = takeAfterDelayInSeconds)
        }

        return when (response.status) {
            HttpStatusCode.OK -> { // Code: 200
                val pickUpPlayerResponseDto = response.body<PickUpPlayerResponseDto>()
                val playerDto = pickUpPlayerResponseDto.playerDto

                val isPlayerPickedUpSuccessfully = playerDto != null
                if (isPlayerPickedUpSuccessfully) {
                    pickedUpPlayerId += 1
                    val playerEntity = playerDto!!.toPlayerEntity().copy(
                        id = pickedUpPlayerId * 100
                    )
                    latestPlayerEntities.add(playerEntity)
                    Result.Success(playerEntity.toPlayer())
                } else {
                    val pickUpPlayerError = when (pickUpPlayerResponseDto.error) {
                        Constants.ERROR_DSFUT_BLOCK -> PickUpPlayerError.Network.DsfutBlock(message = pickUpPlayerResponseDto.message)
                        Constants.ERROR_DSFUT_EMPTY -> PickUpPlayerError.Network.DsfutEmpty
                        Constants.ERROR_DSFUT_LIMIT -> PickUpPlayerError.Network.DsfutLimit
                        Constants.ERROR_DSFUT_MAINTENANCE -> PickUpPlayerError.Network.DsfutMaintenance
                        Constants.ERROR_DSFUT_PARAMETERS -> PickUpPlayerError.Network.DsfutParameters
                        Constants.ERROR_DSFUT_SIGNATURE -> PickUpPlayerError.Network.DsfutSignature
                        Constants.ERROR_DSFUT_AUTHORIZATION -> PickUpPlayerError.Network.DsfutAuthorization
                        Constants.ERROR_DSFUT_THROTTLE -> PickUpPlayerError.Network.DsfutThrottle
                        Constants.ERROR_DSFUT_UNIX_TIME -> PickUpPlayerError.Network.DsfutUnixTime
                        else -> PickUpPlayerError.Network.SomethingWentWrong
                    }

                    Result.Error(pickUpPlayerError)
                }
            }
            else -> Result.Error(PickUpPlayerError.Network.SomethingWentWrong)
        }
    }
}
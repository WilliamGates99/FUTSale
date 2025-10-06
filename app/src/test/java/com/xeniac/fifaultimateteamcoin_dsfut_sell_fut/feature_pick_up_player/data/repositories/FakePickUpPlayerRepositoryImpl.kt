package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.entities.PlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mappers.toPlayer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories.FakeMD5HashGenerator
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.createKtorTestClient
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.HashGenerator
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.mappers.toPlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.remote.PickUpPlayerResponseDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.remote.PlayerDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.errors.PickUpPlayerError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.serialization.json.Json
import kotlin.random.Random

class FakePickUpPlayerRepositoryImpl(
    private val md5HashGenerator: HashGenerator = FakeMD5HashGenerator()
) : PickUpPlayerRepository {

    private var isNetworkAvailable = true
    private var pickUpPlayerHttpStatusCode = HttpStatusCode.OK
    private var isPlayersQueueEmpty = false

    private var pickedUpPlayerId = 0L
    private var latestPlayerEntities = mutableListOf<PlayerEntity>()

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

        val timestampInSeconds = DateHelper.getCurrentTimeInSeconds()
        val signature = md5HashGenerator.generateHash(
            input = partnerId.trim() + secretKey.trim() + timestampInSeconds
        )

        val httpResponse = createKtorTestClient(mockEngine = mockEngine).get(
            urlString = PickUpPlayerRepository.EndPoints.PickUpPlayer(
                platform = Platform.CONSOLE.value,
                partnerId = partnerId.trim(),
                timestamp = timestampInSeconds,
                signature = signature
            ).url
        ) {
            parameter(key = "min_buy", value = minPrice?.trim())
            parameter(key = "max_buy", value = maxPrice?.trim())
            parameter(key = "take_after", value = takeAfterDelayInSeconds)
        }

        return when (httpResponse.status) {
            HttpStatusCode.OK -> { // Code: 200
                val responseDto = httpResponse.body<PickUpPlayerResponseDto>()

                val isPlayerPickedUpSuccessfully = responseDto.playerDto != null
                if (isPlayerPickedUpSuccessfully) {
                    pickedUpPlayerId += 1
                    val playerEntity = responseDto.playerDto!!.toPlayerEntity().copy(
                        id = pickedUpPlayerId * 100
                    )
                    latestPlayerEntities.add(playerEntity)
                    return Result.Success(playerEntity.toPlayer())
                }

                val pickUpPlayerError = when (responseDto.error) {
                    Constants.ERROR_DSFUT_BLOCK -> PickUpPlayerError.Network.DsfutBlock(message = responseDto.message)
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

                return Result.Error(pickUpPlayerError)
            }
            else -> Result.Error(PickUpPlayerError.Network.SomethingWentWrong)
        }
    }
}
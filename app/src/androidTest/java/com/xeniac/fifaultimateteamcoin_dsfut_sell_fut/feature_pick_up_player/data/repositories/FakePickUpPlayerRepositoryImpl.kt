package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mappers.toPlayer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.createKtorTestClient
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.MD5HashGeneratorQualifier
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.HashGenerator
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.mappers.toPlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.remote.PickUpPlayerResponseDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.DummyPlayersHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.errors.PickUpPlayerError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import dagger.Lazy
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
import javax.inject.Inject

class FakePickUpPlayerRepositoryImpl @Inject constructor(
    @MD5HashGeneratorQualifier private val md5HashGenerator: Lazy<HashGenerator>
) : PickUpPlayerRepository {

    private var isNetworkAvailable = true
    private var pickUpPlayerHttpStatusCode = HttpStatusCode.OK
    private var isPlayersQueueEmpty = false

    private var pickedUpPlayerId = 0L

    fun isNetworkAvailable(isAvailable: Boolean) {
        isNetworkAvailable = isAvailable
    }

    fun setPickUpPlayerHttpStatusCode(httpStatusCode: HttpStatusCode) {
        pickUpPlayerHttpStatusCode = httpStatusCode
    }

    fun setIsPlayersQueueEmpty(isEmpty: Boolean) {
        isPlayersQueueEmpty = isEmpty
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
                        playerDto = DummyPlayersHelper.dummyPlayerDto
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
        val signature = md5HashGenerator.get().generateHash(
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
                    DummyPlayersHelper.latestPlayerEntities.add(playerEntity)
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
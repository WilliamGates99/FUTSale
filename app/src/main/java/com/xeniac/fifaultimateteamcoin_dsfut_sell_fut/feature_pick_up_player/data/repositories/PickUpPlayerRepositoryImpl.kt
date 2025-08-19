package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PlayersDao
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.mappers.toPlayer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.MD5HashGeneratorQualifier
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.HashGenerator
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.dto.PickUpPlayerResponseDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.mappers.toPlayerEntity
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.errors.PickUpPlayerError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import dagger.Lazy
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first
import kotlinx.serialization.SerializationException
import timber.log.Timber
import java.net.UnknownHostException
import java.security.cert.CertPathValidatorException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException
import kotlin.coroutines.coroutineContext

class PickUpPlayerRepositoryImpl @Inject constructor(
    private val httpClient: Lazy<HttpClient>,
    private val dsfutDataStoreRepository: Lazy<DsfutDataStoreRepository>,
    private val playersDao: Lazy<PlayersDao>,
    @MD5HashGeneratorQualifier private val md5HashGenerator: Lazy<HashGenerator>
) : PickUpPlayerRepository {

    override suspend fun pickUpPlayer(
        partnerId: String,
        secretKey: String,
        minPrice: String?,
        maxPrice: String?,
        takeAfterDelayInSeconds: Int?
    ): Result<Player, PickUpPlayerError> {
        return try {
            val selectedPlatform = dsfutDataStoreRepository.get().getSelectedPlatform().first()
            val timestampInSeconds = DateHelper.getCurrentTimeInSeconds()
            val signature = md5HashGenerator.get().generateHash(
                input = partnerId.trim() + secretKey.trim() + timestampInSeconds
            )

            val httpResponse = httpClient.get().get(
                urlString = PickUpPlayerRepository.EndPoints.PickUpPlayer(
                    platform = selectedPlatform.value,
                    partnerId = partnerId.trim(),
                    timestamp = timestampInSeconds,
                    signature = signature
                ).url
            ) {
                parameter(key = "min_buy", value = minPrice?.trim())
                parameter(key = "max_buy", value = maxPrice?.trim())
                parameter(key = "take_after", value = takeAfterDelayInSeconds)
            }

            Timber.i("Pick up player response call = ${httpResponse.request.call}")

            when (httpResponse.status) {
                HttpStatusCode.OK -> { // Code: 200
                    val responseDto = httpResponse.body<PickUpPlayerResponseDto>()

                    val isPlayerPickedUpSuccessfully = responseDto.playerDto != null
                    if (isPlayerPickedUpSuccessfully) {
                        val playerEntity = responseDto.playerDto!!.copy(
                            platform = selectedPlatform
                        ).toPlayerEntity()

                        val insertedPlayerId = playersDao.get().insertPlayer(playerEntity)

                        val player = playerEntity.copy(
                            id = insertedPlayerId
                        ).toPlayer()

                        return Result.Success(player)
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
        } catch (e: UnresolvedAddressException) { // When device is offline
            Timber.e("Pick up player UnresolvedAddressException:")
            e.printStackTrace()
            Result.Error(PickUpPlayerError.Network.Offline)
        } catch (e: UnknownHostException) { // When device is offline
            Timber.e("Pick up player UnknownHostException:")
            e.printStackTrace()
            Result.Error(PickUpPlayerError.Network.Offline)
        } catch (e: ConnectTimeoutException) {
            Timber.e("Pick up player ConnectTimeoutException:")
            e.printStackTrace()
            Result.Error(PickUpPlayerError.Network.ConnectTimeoutException)
        } catch (e: HttpRequestTimeoutException) {
            Timber.e("Pick up player HttpRequestTimeoutException:")
            e.printStackTrace()
            Result.Error(PickUpPlayerError.Network.HttpRequestTimeoutException)
        } catch (e: SocketTimeoutException) {
            Timber.e("Pick up player SocketTimeoutException:")
            e.printStackTrace()
            Result.Error(PickUpPlayerError.Network.SocketTimeoutException)
        } catch (e: RedirectResponseException) { // 3xx responses
            Timber.e("Pick up player RedirectResponseException:")
            e.printStackTrace()
            Result.Error(PickUpPlayerError.Network.RedirectResponseException)
        } catch (e: ClientRequestException) { // 4xx responses
            Timber.e("Pick up player ClientRequestException:")
            e.printStackTrace()
            when (e.response.status) {
                HttpStatusCode.TooManyRequests -> Result.Error(PickUpPlayerError.Network.TooManyRequests)
                else -> Result.Error(PickUpPlayerError.Network.ClientRequestException)
            }
        } catch (e: ServerResponseException) { // 5xx responses
            Timber.e("Pick up player ServerResponseException:")
            e.printStackTrace()
            Result.Error(PickUpPlayerError.Network.ServerResponseException)
        } catch (e: SerializationException) {
            Timber.e("Pick up player SerializationException:")
            e.printStackTrace()
            Result.Error(PickUpPlayerError.Network.SerializationException)
        } catch (e: JsonConvertException) {
            Timber.e("Pick up player JsonConvertException:")
            e.printStackTrace()
            Result.Error(PickUpPlayerError.Network.JsonConvertException)
        } catch (e: SSLHandshakeException) {
            Timber.e("Pick up player SSLHandshakeException:")
            e.printStackTrace()
            Result.Error(PickUpPlayerError.Network.SSLHandshakeException)
        } catch (e: CertPathValidatorException) {
            Timber.e("Pick up player CertPathValidatorException:")
            e.printStackTrace()
            Result.Error(PickUpPlayerError.Network.CertPathValidatorException)
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            Timber.e("Pick up player Exception:")
            e.printStackTrace()
            Result.Error(PickUpPlayerError.Network.SomethingWentWrong)
        }
    }
}
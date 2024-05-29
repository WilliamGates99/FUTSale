package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.db.PlayersDao
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.dto.PickUpPlayerResponseDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.HashHelper.getMd5Signature
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.utils.PickUpPlayerError
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
import io.ktor.util.network.UnresolvedAddressException
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

class PickUpPlayerRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val playerDao: Lazy<PlayersDao>
) : PickUpPlayerRepository {

    override suspend fun pickUpPlayer(
        platform: String,
        partnerId: String,
        secretKey: String,
        minPrice: String?,
        maxPrice: String?,
        takeAfter: Int?
    ): Result<Player, PickUpPlayerError> = try {
        val timestamp = DateHelper.getCurrentTimeInMillis()
        val signature = getMd5Signature(partnerId, secretKey, timestamp)

        val response = httpClient.get(
            urlString = PickUpPlayerRepository.EndPoints.PickUpPlayer(
                platform = platform,
                partnerId = partnerId,
                timestamp = timestamp,
                signature = signature
            ).url
        ) {
            parameter(key = "min_buy", value = minPrice)
            parameter(key = "max_buy", value = maxPrice)
            parameter(key = "take_after", value = takeAfter)
        }

        Timber.i("Pick up player response call = ${response.request.call}")

        when (response.status.value) {
            HttpStatusCode.OK.value -> { // Code: 200
                val pickUpPlayerResponseDto = response.body<PickUpPlayerResponseDto>()
                val playerDto = pickUpPlayerResponseDto.playerDto

                val isPlayerPickedUpSuccessfully = playerDto != null
                if (isPlayerPickedUpSuccessfully) {
                    playerDao.get().insertPlayer(playerDto!!.toPlayerEntity())
                    Result.Success(playerDto.toPlayer())
                } else {
                    val pickUpPlayerError = when (pickUpPlayerResponseDto.error) {
                        Constants.ERROR_DSFUT_BLOCK -> PickUpPlayerError.Network.DsfutBlock
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
    } catch (e: UnresolvedAddressException) { // When device is offline
        Timber.e("Pick up player UnresolvedAddressException:}")
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
        Result.Error(PickUpPlayerError.Network.ClientRequestException)
    } catch (e: ServerResponseException) { // 5xx responses
        Timber.e("Pick up player ServerResponseException:")
        e.printStackTrace()
        Result.Error(PickUpPlayerError.Network.ServerResponseException)
    } catch (e: Exception) {
        Timber.e("Pick up player Exception:")
        e.printStackTrace()
        if (e.message?.lowercase(Locale.US)?.contains("unable to resolve host") == true) {
            Result.Error(PickUpPlayerError.Network.Offline)
        } else Result.Error(PickUpPlayerError.Network.SomethingWentWrong)
    }
}
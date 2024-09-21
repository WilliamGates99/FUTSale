package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.repositories

import android.os.CountDownTimer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.local.PlayersDao
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.utils.DateHelper
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.dto.PickUpPlayerResponseDto
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.Constants
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.DateHelper.isPickedPlayerExpired
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.data.utils.HashHelper.getMd5Signature
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.TimerValueInSeconds
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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class PickUpPlayerRepositoryImpl @Inject constructor(
    private val httpClient: Lazy<HttpClient>,
    private val preferencesRepository: Lazy<PreferencesRepository>,
    private val playersDao: Lazy<PlayersDao>
) : PickUpPlayerRepository {

    override fun observeLatestPickedPlayers(): Flow<List<Player>> = playersDao.get()
        .observeLatestPickedPlayers(
            currentTimeInSeconds = DateHelper.getCurrentTimeInSeconds()
        ).map { playerEntities ->
            playerEntities.map { it.toPlayer() }
        }

    override fun observePickedUpPlayer(playerId: Long): Flow<Player> = playersDao.get()
        .observerPlayer(id = playerId)
        .map { it.toPlayer() }

    override fun observeCountDownTimer(expiryTimeInMs: Long): Flow<TimerValueInSeconds> =
        callbackFlow {
            val isPlayerExpired = isPickedPlayerExpired(expiryTimeInMs)
            if (isPlayerExpired) {
                send(0)
                close()
                return@callbackFlow
            }

            val timerStartTimeInMs = expiryTimeInMs - DateHelper.getCurrentTimeInMillis()
            val countDownTimer = object : CountDownTimer(
                /* millisInFuture = */ timerStartTimeInMs,
                /* countDownInterval = */ Constants.COUNT_DOWN_TIMER_INTERVAL_IN_MS
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    trySend((millisUntilFinished / 1000).toInt())
                }

                override fun onFinish() {
                    Timber.i("Player Expiry timer is finished.")
                    trySend(0)
                    close()
                }
            }.start()

            awaitClose { countDownTimer?.cancel() }
        }

    override suspend fun pickUpPlayer(
        partnerId: String,
        secretKey: String,
        minPrice: String?,
        maxPrice: String?,
        takeAfterDelayInSeconds: Int?
    ): Result<Player, PickUpPlayerError> = try {
        val selectedPlatform = preferencesRepository.get().getSelectedPlatform().first()
        val timestampInSeconds = DateHelper.getCurrentTimeInSeconds()
        val signature = getMd5Signature(
            partnerId = partnerId,
            secretKey = secretKey,
            timestamp = timestampInSeconds
        )

        val response = httpClient.get().get(
            urlString = PickUpPlayerRepository.EndPoints.PickUpPlayer(
                platform = selectedPlatform.value,
                partnerId = partnerId,
                timestamp = timestampInSeconds,
                signature = signature
            ).url
        ) {
            parameter(key = "min_buy", value = minPrice)
            parameter(key = "max_buy", value = maxPrice)
            parameter(key = "take_after", value = takeAfterDelayInSeconds)
        }

        Timber.i("Pick up player response call = ${response.request.call}")

        when (response.status) {
            HttpStatusCode.OK -> { // Code: 200
                val pickUpPlayerResponseDto = response.body<PickUpPlayerResponseDto>()
                val playerDto = pickUpPlayerResponseDto.playerDto

                val isPlayerPickedUpSuccessfully = playerDto != null
                if (isPlayerPickedUpSuccessfully) {
                    val playerEntity = playerDto!!.copy(
                        platform = selectedPlatform
                    ).toPlayerEntity()
                    val playerId = playersDao.get().insertPlayer(playerEntity)

                    val player = playerEntity.copy(
                        id = playerId
                    ).toPlayer()

                    Result.Success(player)
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
        Result.Error(PickUpPlayerError.Network.ClientRequestException)
    } catch (e: ServerResponseException) { // 5xx responses
        Timber.e("Pick up player ServerResponseException:")
        e.printStackTrace()
        Result.Error(PickUpPlayerError.Network.ServerResponseException)
    } catch (e: SerializationException) {
        Timber.e("Pick up player SerializationException:")
        e.printStackTrace()
        Result.Error(PickUpPlayerError.Network.SerializationException)
    } catch (e: Exception) {
        coroutineContext.ensureActive()

        Timber.e("Pick up player Exception:")
        e.printStackTrace()
        Result.Error(PickUpPlayerError.Network.SomethingWentWrong)
    }
}
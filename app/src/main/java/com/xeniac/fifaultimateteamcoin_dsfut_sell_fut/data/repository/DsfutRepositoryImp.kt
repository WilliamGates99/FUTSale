package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository

import androidx.lifecycle.LiveData
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.Resource
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util.UiText
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local.DsfutDao
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.local.models.PickedUpPlayer
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.DsfutApi
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.remote.models.Player
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.DsfutRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.DateHelper
import timber.log.Timber
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

class DsfutRepositoryImp @Inject constructor(
    private val dsfutDao: DsfutDao,
    private val dsfutApi: DsfutApi
) : DsfutRepository {

    override suspend fun insertPickedUpPlayer(pickedUpPlayer: PickedUpPlayer) =
        dsfutDao.insertPickedUpPlayer(pickedUpPlayer)

    override suspend fun deletePickedUpPlayer(pickedUpPlayer: PickedUpPlayer) =
        dsfutDao.deletePickedUpPlayer(pickedUpPlayer)

    override fun observeAllPickedUpPlayers(): LiveData<List<PickedUpPlayer>> =
        dsfutDao.observeAllPickedUpPlayers()

    override suspend fun pickUpPlayer(
        platform: String,
        partnerId: String,
        secretKey: String,
        minPrice: Int?,
        maxPrice: Int?,
        takeAfter: Int?,
        fifaVersion: Int
    ): Resource<Player> = try {
        val timestamp = DateHelper.getCurrentTimeInMillis()
        val signature = getMd5Signature(partnerId, secretKey, timestamp)

        /*
        val response = dsfutApi.pickUpPlayer(
            fifaVersion = fifaVersion,
            platform = platform,
            partnerId = partnerId,
            timestamp = timestamp,
            signature = signature,
            minPrice = minPrice,
            maxPrice = maxPrice,
            takeAfter = takeAfter
        )

        response.body()?.let {
            Timber.i("pickUpPlayer Response: $it")

            if (!it.error.isNullOrBlank()) {
                Timber.e("pickUpPlayer Error: ${it.error}")
                Timber.e("pickUpPlayer Message: ${it.message}")
                return when {
                    it.error.contains(Constants.ERROR_DSFUT_BLOCK) -> {
                        val errorMessage = "${it.error} - ${it.message}"
                        Resource.Error(UiText.DynamicString(errorMessage))
                    }
                    it.error.contains(Constants.ERROR_DSFUT_EMPTY) -> Resource.Error(
                        UiText.StringResource(R.string.pick_up_error_dsfut_empty)
                    )
                    it.error.contains(Constants.ERROR_DSFUT_LIMIT) -> Resource.Error(
                        UiText.StringResource(R.string.pick_up_error_dsfut_limit)
                    )
                    it.error.contains(Constants.ERROR_DSFUT_MAINTENANCE) -> Resource.Error(
                        UiText.StringResource(R.string.pick_up_error_dsfut_maintenance)
                    )
                    it.error.contains(Constants.ERROR_DSFUT_SIGN) -> Resource.Error(
                        UiText.StringResource(R.string.pick_up_error_dsfut_sign)
                    )
                    it.error.contains(Constants.ERROR_DSFUT_THROTTLE) -> Resource.Error(
                        UiText.StringResource(R.string.pick_up_error_dsfut_throttle)
                    )
                    else -> Resource.Error(UiText.StringResource(R.string.error_something_went_wrong))
                }
            }

            return it.player?.let { player ->
                Timber.i("pickUpPlayer Player: $player")
                Resource.Success(player)
            } ?: Resource.Error(UiText.StringResource(R.string.error_something_went_wrong))
        } ?: Resource.Error(UiText.StringResource(R.string.error_something_went_wrong))
        */
        Resource.Error(UiText.StringResource(R.string.error_something_went_wrong)) // TEMP
    } catch (e: Exception) {
        Timber.e("pickUpPlayer Exception: ${e.message}")
        Resource.Error(UiText.DynamicString(e.message.toString()))
    }

    private fun getMd5Signature(partnerId: String, secretKey: String, timestamp: String): String {
        val input = partnerId + secretKey + timestamp
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(23, '0')
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.models.PickUpPlayerResult
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateMaxPrice
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateMinPrice
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidatePartnerId
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateSecretKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateTakeAfter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PickUpPlayerUseCase(
    private val dsfutDataStoreRepository: DsfutDataStoreRepository,
    private val pickUpPlayerRepository: PickUpPlayerRepository,
    private val validatePartnerId: ValidatePartnerId,
    private val validateSecretKey: ValidateSecretKey,
    private val validateMinPrice: ValidateMinPrice,
    private val validateMaxPrice: ValidateMaxPrice,
    private val validateTakeAfter: ValidateTakeAfter
) {
    operator fun invoke(
        minPrice: String? = null,
        maxPrice: String? = null,
        takeAfterDelayInSeconds: Int? = null
    ): Flow<PickUpPlayerResult> = flow {
        val partnerId = dsfutDataStoreRepository.getPartnerId()
        val secretKey = dsfutDataStoreRepository.getSecretKey()

        val partnerIdError = validatePartnerId(partnerId)
        val secretKeyError = validateSecretKey(secretKey)
        val minPriceError = validateMinPrice(minPrice)
        val maxPriceError = validateMaxPrice(maxPrice)
        val takeAfterError = takeAfterDelayInSeconds?.let { validateTakeAfter(it) }

        val hasError = listOf(
            partnerIdError,
            secretKeyError,
            minPriceError,
            maxPriceError,
            takeAfterError
        ).any { it != null }

        if (hasError) {
            return@flow emit(
                PickUpPlayerResult(
                    partnerIdError = partnerIdError,
                    secretKeyError = secretKeyError,
                    minPriceError = minPriceError,
                    maxPriceError = maxPriceError,
                    takeAfterError = takeAfterError
                )
            )
        }

        return@flow emit(
            PickUpPlayerResult(
                result = pickUpPlayerRepository.pickUpPlayer(
                    partnerId = partnerId.orEmpty(),
                    secretKey = secretKey.orEmpty(),
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    takeAfterDelayInSeconds = takeAfterDelayInSeconds,
                )
            )
        )
    }
}
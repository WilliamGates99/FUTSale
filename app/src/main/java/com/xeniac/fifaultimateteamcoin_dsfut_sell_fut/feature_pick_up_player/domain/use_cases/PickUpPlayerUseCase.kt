package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.models.PickUpPlayerResult
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.repositories.PickUpPlayerRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateMaxPrice
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateMinPrice
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidatePartnerId
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateSecretKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_pick_up_player.domain.validation.ValidateTakeAfter

class PickUpPlayerUseCase(
    private val preferencesRepository: PreferencesRepository,
    private val pickUpPlayerRepository: PickUpPlayerRepository,
    private val validatePartnerId: ValidatePartnerId,
    private val validateSecretKey: ValidateSecretKey,
    private val validateMinPrice: ValidateMinPrice,
    private val validateMaxPrice: ValidateMaxPrice,
    private val validateTakeAfter: ValidateTakeAfter
) {
    suspend operator fun invoke(
        minPrice: String? = null,
        maxPrice: String? = null,
        takeAfter: Int? = null
    ): PickUpPlayerResult {
        val partnerId = preferencesRepository.getPartnerId()
        val secretKey = preferencesRepository.getSecretKey()

        val partnerIdError = validatePartnerId(partnerId)
        val secretKeyError = validateSecretKey(secretKey)
        val minPriceError = validateMinPrice(minPrice)
        val maxPriceError = validateMaxPrice(maxPrice)
        val takeAfterError = takeAfter?.let { validateTakeAfter(it) }

        val hasError = listOf(
            partnerIdError,
            secretKeyError,
            minPriceError,
            maxPriceError,
            takeAfterError
        ).any { it != null }

        if (hasError) {
            return PickUpPlayerResult(
                partnerIdError = partnerIdError,
                secretKeyError = secretKeyError,
                minPriceError = minPriceError,
                maxPriceError = maxPriceError,
                takeAfterError = takeAfterError
            )
        }

        return PickUpPlayerResult(
            result = pickUpPlayerRepository.pickUpPlayer(
                partnerId = partnerId ?: "",
                secretKey = secretKey ?: "",
                minPrice = minPrice,
                maxPrice = maxPrice,
                takeAfter = takeAfter,
            )
        )
    }
}
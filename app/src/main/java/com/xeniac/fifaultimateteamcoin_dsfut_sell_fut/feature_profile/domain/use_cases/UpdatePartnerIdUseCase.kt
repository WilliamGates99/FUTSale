package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.models.UpdatePartnerIdResult
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.PartnerIdError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation.ValidatePartnerId

class UpdatePartnerIdUseCase(
    private val preferencesRepository: PreferencesRepository,
    private val validatePartnerId: ValidatePartnerId
) {
    suspend operator fun invoke(partnerId: String): UpdatePartnerIdResult {
        try {
            val partnerIdError = validatePartnerId(partnerId)

            val hasError = listOf(
                partnerIdError
            ).any { it != null }

            if (hasError) {
                return UpdatePartnerIdResult(
                    partnerIdError = partnerIdError
                )
            }

            preferencesRepository.storePartnerId(partnerId = partnerId)

            return UpdatePartnerIdResult(
                result = Result.Success(Unit)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return UpdatePartnerIdResult(
                result = Result.Error(PartnerIdError.SomethingWentWrong)
            )
        }
    }
}
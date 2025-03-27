package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.models.UpdatePartnerIdResult
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.UpdatePartnerIdError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation.ValidatePartnerId
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdatePartnerIdUseCase(
    private val dsfutDataStoreRepository: DsfutDataStoreRepository,
    private val validatePartnerId: ValidatePartnerId
) {
    operator fun invoke(
        partnerId: String,
        delayTimeInMillis: Long = 500
    ): Flow<UpdatePartnerIdResult> = flow {
        return@flow try {
            delay(timeMillis = delayTimeInMillis)

            val partnerIdError = validatePartnerId(partnerId)

            val hasError = listOf(
                partnerIdError
            ).any { it != null }

            if (hasError) {
                return@flow emit(
                    UpdatePartnerIdResult(
                        updatePartnerIdError = partnerIdError
                    )
                )
            }

            dsfutDataStoreRepository.storePartnerId(partnerId = partnerId)

            emit(UpdatePartnerIdResult(result = Result.Success(Unit)))
        } catch (e: Exception) {
            emit(
                UpdatePartnerIdResult(
                    result = Result.Error(UpdatePartnerIdError.SomethingWentWrong)
                )
            )
        }
    }
}
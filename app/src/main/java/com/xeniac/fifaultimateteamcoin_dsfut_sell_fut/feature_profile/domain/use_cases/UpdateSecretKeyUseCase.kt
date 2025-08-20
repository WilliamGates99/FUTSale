package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.errors.UpdateSecretKeyError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.models.UpdateSecretKeyResult
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation.ValidateSecretKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateSecretKeyUseCase(
    private val dsfutDataStoreRepository: DsfutDataStoreRepository,
    private val validateSecretKey: ValidateSecretKey
) {
    operator fun invoke(
        secretKey: String,
        delayTimeInMillis: Long = 500
    ): Flow<UpdateSecretKeyResult> = flow {
        return@flow try {
            delay(timeMillis = delayTimeInMillis)

            val secretKeyError = validateSecretKey(secretKey)

            val hasError = listOf(
                secretKeyError
            ).any { it != null }

            if (hasError) {
                return@flow emit(
                    UpdateSecretKeyResult(
                        updateSecretKeyError = secretKeyError
                    )
                )
            }

            dsfutDataStoreRepository.storeSecretKey(secretKey = secretKey)

            emit(UpdateSecretKeyResult(result = Result.Success(Unit)))
        } catch (e: Exception) {
            emit(
                UpdateSecretKeyResult(
                    result = Result.Error(UpdateSecretKeyError.SomethingWentWrong)
                )
            )
        }
    }
}
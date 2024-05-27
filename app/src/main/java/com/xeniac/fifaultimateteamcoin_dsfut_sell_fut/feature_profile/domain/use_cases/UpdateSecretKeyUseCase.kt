package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.models.UpdateSecretKeyResult
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.SecretKeyError
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.validation.ValidateSecretKey

class UpdateSecretKeyUseCase(
    private val preferencesRepository: PreferencesRepository,
    private val validateSecretKey: ValidateSecretKey
) {
    suspend operator fun invoke(secretKey: String): UpdateSecretKeyResult {
        try {
            val secretKeyError = validateSecretKey(secretKey)

            val hasError = listOf(
                secretKeyError
            ).any { it != null }

            if (hasError) {
                return UpdateSecretKeyResult(
                    secretKeyError = secretKeyError
                )
            }

            preferencesRepository.setSecretKey(secretKey = secretKey)

            return UpdateSecretKeyResult(
                result = Result.Success(Unit)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return UpdateSecretKeyResult(
                result = Result.Error(SecretKeyError.SomethingWentWrong)
            )
        }
    }
}
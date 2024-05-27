package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.use_cases

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_profile.domain.utils.SecretKeyError

class UpdateSecretKeyUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(secretKey: String): Result<Unit, SecretKeyError> = try {
        preferencesRepository.setSecretKey(secretKey = secretKey)
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(SecretKeyError.SomethingWentWrong)
    }
}
package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.utils.OnboardingError

class CompleteOnboardingUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(
        partnerId: String?,
        secretKey: String?
    ): Result<Unit, OnboardingError> = try {
        preferencesRepository.storePartnerId(partnerId = partnerId)
        preferencesRepository.storeSecretKey(secretKey = secretKey)
        preferencesRepository.isOnBoardingCompleted(isCompleted = true)
        Result.Success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(OnboardingError.SomethingWentWrong)
    }
}
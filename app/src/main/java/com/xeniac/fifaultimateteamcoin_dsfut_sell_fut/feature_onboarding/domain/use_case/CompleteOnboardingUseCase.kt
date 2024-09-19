package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.utils.OnboardingError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CompleteOnboardingUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(
        partnerId: String?,
        secretKey: String?
    ): Flow<Result<Unit, OnboardingError>> = flow {
        try {
            preferencesRepository.storePartnerId(partnerId = partnerId)
            preferencesRepository.storeSecretKey(secretKey = secretKey)
            preferencesRepository.isOnBoardingCompleted(isCompleted = true)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(OnboardingError.SomethingWentWrong))
        }
    }
}
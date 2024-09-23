package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.SettingsDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Result
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.utils.OnboardingError
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class CompleteOnboardingUseCase(
    private val settingsDataStoreRepository: SettingsDataStoreRepository,
    private val dsfutDataStoreRepository: DsfutDataStoreRepository
) {
    operator fun invoke(
        partnerId: String?,
        secretKey: String?
    ): Flow<Result<Unit, OnboardingError>> = callbackFlow {
        try {
            dsfutDataStoreRepository.storePartnerId(partnerId = partnerId)
            dsfutDataStoreRepository.storeSecretKey(secretKey = secretKey)
            settingsDataStoreRepository.isOnboardingCompleted(isCompleted = true)
            send(Result.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            send(Result.Error(OnboardingError.SomethingWentWrong))
        }

        awaitClose { }
    }
}
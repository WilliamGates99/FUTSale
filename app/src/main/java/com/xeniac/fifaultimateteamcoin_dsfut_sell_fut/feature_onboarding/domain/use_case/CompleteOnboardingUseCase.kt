package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.feature_onboarding.domain.use_case

import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.R
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.Resource
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.UiText

class CompleteOnboardingUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(
        partnerId: String?,
        secretKey: String?
    ): Resource<Nothing> = try {
        preferencesRepository.setPartnerId(partnerId = partnerId)
        preferencesRepository.setSecretKey(secretKey = secretKey)
        preferencesRepository.isOnBoardingCompleted(isCompleted = true)
        Resource.Success()
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.Error(UiText.StringResource(R.string.error_something_went_wrong))
    }
}